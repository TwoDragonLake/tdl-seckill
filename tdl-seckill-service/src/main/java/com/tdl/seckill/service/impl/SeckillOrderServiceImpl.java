package com.tdl.seckill.service.impl;

import com.tdl.seckill.api.SeckillOrderService;
import com.tdl.seckill.mapper.SeckillGoodsDoMapper;
import com.tdl.seckill.mapper.SeckillOrderDoMapper;
import com.tdl.seckill.dos.SeckillGoodsDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/6 17:50
 */

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    SeckillOrderDoMapper seckillOrderDoMapper;
    @Autowired
    SeckillGoodsDoMapper seckillGoodsDoMapper;

    @Override
    public void subStockAndPushToMQ(Long goodId, Long userId) {

    }

    @Override
    public List<SeckillGoodsDo> getSeckillGoods() {
        return null;
    }
}
