package com.tdl.seckill.biz.impl;

import com.alibaba.fastjson.JSON;
import com.tdl.seckill.biz.SeckillOrderBiz;
import com.tdl.seckill.bo.SeckillGoodsBO;
import com.tdl.seckill.common.constant.Constants;
import com.tdl.seckill.common.enums.SeckillGoodsStatusEnum;
import com.tdl.redis.access.RedisLimit;
import com.tdl.seckill.config.kafka.KafkaConfig;
import com.tdl.seckill.mapper.SeckillGoodsDoMapper;
import com.tdl.seckill.mapper.SeckillOrderDoMapper;
import com.tdl.seckill.dos.SeckillGoodsDo;
import com.tdl.seckill.dos.SeckillGoodsDoExample;
import com.tdl.seckill.dos.SeckillOrderDo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/6 18:47
 */

@Component
public class SeckillOrderBizImpl implements SeckillOrderBiz {
    @Autowired
    SeckillOrderDoMapper seckillOrderDoMapper;
    @Autowired
    SeckillGoodsDoMapper seckillGoodsDoMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate ;

    @Autowired
    private RedisLimit redisLimit;

  //  @Autowired
   // private KafkaProducer<String, SeckillGoodsBO> kafkaProducer ;

    @Autowired
    KafkaConfig kafkaConfig;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void subStockAndPushToMQ(Long goodId, Long userId) {
        //1、校验库存
       /* SeckillGoodsDo seckillGoodsDo  = seckillGoodsDoMapper.selectByPrimaryKey(goodId);
        if(seckillGoodsDo.getSale().longValue()==seckillGoodsDo.getTotal().longValue()){
            throw new RuntimeException("库存不足");
        }*/

        //2、减库存
        /*seckillGoodsDo.setSale(seckillGoodsDo.getSale() + 1);
        int count =  seckillGoodsDoMapper.updateOptimistic(seckillGoodsDo);
        if(count <= 0){
            throw new RuntimeException("扣减库存失败");
        }*/

        //3、创建订单
        /*SeckillOrderDo seckillOrderDo = new SeckillOrderDo();
        seckillOrderDo.setGoodsId(seckillGoodsDo.getId());
        seckillOrderDo.setName(seckillGoodsDo.getName());
        seckillOrderDo.setUserId(userId);
        return seckillOrderDoMapper.insert(seckillOrderDo);*/


        Long total  =  Long.parseLong(redisTemplate.opsForValue().get(Constants.STOCK_COUNT + goodId));
        Long sale  =  Long.parseLong(redisTemplate.opsForValue().get(Constants.STOCK_SALE + goodId));

        if(sale.longValue() >= total.longValue()){
            throw new RuntimeException("库存不足");
        }
        Long version  =  Long.parseLong(redisTemplate.opsForValue().get(Constants.STOCK_VERSION + goodId));
        SeckillGoodsBO seckillGoodsBO = new SeckillGoodsBO();
        seckillGoodsBO.setId(goodId);
        seckillGoodsBO.setSale(sale);
        seckillGoodsBO.setTotal(total);
        seckillGoodsBO.setVersion(version);
        seckillGoodsBO.setUserId(userId);
        kafkaTemplate.send(kafkaConfig.topic, JSON.toJSONString(seckillGoodsBO));
       // kafkaProducer.send(new ProducerRecord(kafkaConfig.topic,seckillGoodsBO));
    }

    @Override
    public List<SeckillGoodsDo> getSeckillGoods() {
        SeckillGoodsDoExample seckillGoodsDoExample = new SeckillGoodsDoExample();
        seckillGoodsDoExample.createCriteria().andStatusEqualTo(SeckillGoodsStatusEnum.enabled.getCode());
        return seckillGoodsDoMapper.selectByExample(seckillGoodsDoExample);
    }


    @Override
    public void createOrder(SeckillGoodsDo seckillGoodsDo, Long userId){
        int count  =  seckillGoodsDoMapper.updateByPrimaryKey(seckillGoodsDo);
        if(count <= 0){
            throw new RuntimeException("更新库存失败");
        }

        SeckillGoodsDo updatedSeckillGoodsDo = seckillGoodsDoMapper.selectByPrimaryKey(seckillGoodsDo.getId());
        SeckillOrderDo seckillOrderDo = new SeckillOrderDo();
        seckillOrderDo.setGoodsId(seckillGoodsDo.getId());
        seckillOrderDo.setName(updatedSeckillGoodsDo.getName());
        seckillOrderDo.setUserId(userId);
        seckillOrderDoMapper.insert(seckillOrderDo);

        redisTemplate.opsForValue().increment(Constants.STOCK_SALE + seckillGoodsDo.getId(),1) ;
        redisTemplate.opsForValue().increment(Constants.STOCK_VERSION + seckillGoodsDo.getId(),1) ;
    }

}
