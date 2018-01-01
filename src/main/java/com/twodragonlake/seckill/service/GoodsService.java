/*
 * Copyright (C) 2018 The TwoDragonLake Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twodragonlake.seckill.service;

import com.twodragonlake.message.Message;
import com.twodragonlake.message.MessageTrunk;
import com.twodragonlake.mvc.exception.BusinessException;
import com.twodragonlake.seckill.cache.*;
import com.twodragonlake.seckill.constant.MessageType;
import com.twodragonlake.seckill.dao.GoodsMapper;
import com.twodragonlake.seckill.model.Goods;
import com.twodragonlake.seckill.mq.message.SeckillRequestMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GoodsService.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/2/3 21:21
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsBuyCurrentLimiter goodsBuyCurrentLimiter;

    @Autowired
    private SeckillSuccessTokenCache seckillSuccessTokenCache;

    @Autowired
    private MessageTrunk messageTrunk;

    @Autowired
    private SeckillFinishCache seckillFinishCache;

    @Autowired
    private SeckillHandlingListCache seckillHandlingListCache;

    @Autowired
    private GoodsInfoCacheWorker goodsInfoCacheWorker;

    /**
     * 根据goodsId获取随机名称(这样确保前端用户不能提前知道秒杀链接，降低被刷风险)
     */
    public String getGoodsRandomName(Integer goodsId) {
        Goods goods = goodsInfoCacheWorker.get(goodsId, Goods.class);
        long now = System.currentTimeMillis();

        // 已经开始了活动，则输出抢购链接
        if (goods.getStartTime().getTime() < now && now < goods.getEndTime().getTime()) {
            return goods.getRandomName();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取抢购商品列表
     */
    public List<Goods> getGoodsList() {
        return goodsMapper.selectAll();
    }

    /**
     * 获取商品详情
     */
    public Goods getDetail(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    /**
     * 做秒杀操作
     */
    public void Seckill(String mobile, String goodsRandomName) {
        // 先看抢购是否已经结束了
        if (seckillFinishCache.isFinish(goodsRandomName)) {
            throw new BusinessException("您已经提交抢购，正在处理中");
        }

        // 先限流
        goodsBuyCurrentLimiter.doLimit(goodsRandomName, "啊呀，没挤进去");

        // 判断是否处理中(是否在处理列表中)
        if (seckillHandlingListCache.isInHandleList(mobile, goodsRandomName)) {
            throw new BusinessException("您已经提交过抢购，如果抢购成功请下单，否则耐心等待哦...");
        }

        // 请求消息推入处理队列，结束
        Message message = new Message(MessageType.SECKILL_MESSAGE, new SeckillRequestMessage(mobile, goodsRandomName));
        messageTrunk.put(message);

        // 加入正在处理列表
        seckillHandlingListCache.add2HandleList(mobile, goodsRandomName);

    }

    private void checkStore(String goodsRandomName) {
        Goods goods = goodsMapper.selectByRandomName(goodsRandomName);
        if (goods == null || goods.getStore() <= 0) {
            seckillFinishCache.setFinish(goodsRandomName);
            throw new RuntimeException("很遗憾，抢购已经结束了哟"); // 库存不足，抢购失败
        }
    }

    /**
     * 真正做减库存操作(这里没有采用存储过程的原因：这里并没有高并发，高并发已经在获取token时分流，所以此处没必要用存储过程)
     */
    public Integer reduceStoreAndCreateOrder(String mobile, Integer goodsId) {
        Date orderTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        map.put("mobile", mobile);
        map.put("orderTime", orderTime);
        map.put("o_result", -2);
        map.put("o_order_id", -1);
        goodsMapper.doOrder(map);

        Integer result = (Integer) map.get("o_result");

        if (result != null && result == 1) {
            return (Integer) map.get("o_order_id");

        }
        throw new BusinessException("下单失败，来晚了");
    }

    /**
     * 真正的减库存、下单操作
     */
    public Integer order(String mobile, Integer goodsId, String token) {
        // 先检查token有效性
        Goods goodsInfo = goodsInfoCacheWorker.get(goodsId, Goods.class);
        if (!seckillSuccessTokenCache.validateToken(mobile, goodsInfo.getRandomName(), token)) {
            throw new BusinessException("token不对，不能下单哦");
        }

        // 先检查库存，没有库存直接结束
        checkStore(goodsInfo.getRandomName());

        // 对于进来的客户做减库存+生成订单
        return reduceStoreAndCreateOrder(mobile, goodsId);
    }

}
