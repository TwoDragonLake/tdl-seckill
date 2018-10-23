package com.tdl.seckill.biz.impl;

import com.tdl.seckill.biz.SeckillOrderBiz;
import com.tdl.seckill.bo.SeckillGoodsBO;
import com.tdl.seckill.common.constant.Constants;
import com.tdl.seckill.common.enums.SeckillGoodsStatusEnum;
import com.tdl.redis.access.RedisLimit;
import com.tdl.seckill.mapper.SeckillGoodsDoMapper;
import com.tdl.seckill.mapper.SeckillOrderDoMapper;
import com.tdl.seckill.dos.SeckillGoodsDo;
import com.tdl.seckill.dos.SeckillGoodsDoExample;
import com.tdl.seckill.dos.SeckillOrderDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Override
    public void subStockAndPushToMQ(Long goodId, Long userId) {
        //1、校验库存
       /* SeckillGoodsDo seckillGoodsDo  = seckillGoodsDoMapper.selectByPrimaryKey(goodId);
        if(seckillGoodsDo.getSale().longValue()==seckillGoodsDo.getTotal().longValue()){
            throw new RuntimeException("库存不足");
        }*/

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
    }

    @Override
    public List<SeckillGoodsDo> getSeckillGoods() {
        SeckillGoodsDoExample seckillGoodsDoExample = new SeckillGoodsDoExample();
        seckillGoodsDoExample.createCriteria().andStatusEqualTo(SeckillGoodsStatusEnum.enabled.getCode());
        return seckillGoodsDoMapper.selectByExample(seckillGoodsDoExample);
    }
}
