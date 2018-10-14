package com.tdl.seckill.api;

import com.tdl.seckill.dos.SeckillGoodsDo;

import java.util.List;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/6 17:35
 */
public interface SeckillOrderService {
    /**
     * 执行库存校验、减库存、创建订单操作
     * @param goodId
     * @param userId
     */
    void subStockAndPushToMQ(Long goodId,Long userId);

    /**
     * 罗列商品列表
     * @return
     */
    List<SeckillGoodsDo> getSeckillGoods();
}
