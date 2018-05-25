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

package com.twodragonlake.seckill.cache;

import com.twodragonlake.seckill.cache.base.CacheWorker;
import com.twodragonlake.seckill.constant.CommonConstant;
import com.twodragonlake.seckill.dao.GoodsMapper;
import com.twodragonlake.seckill.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 获取商品信息缓存工作器.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2016/8/26 11:17
 */
@Component
public class GoodsInfoCacheWorker extends CacheWorker<Integer, Goods> {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    protected Goods getDataWhenNoCache(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    protected String getKey(Integer goodsId) {
        return MessageFormat.format(CommonConstant.RedisKey.GOODS_INFO_BY_ID, goodsId);
    }

    @Override
    protected int getExpireSeconds() {
        return CommonConstant.RedisKeyExpireSeconds.GOODS_STORE_BY_ID;
    }

}
