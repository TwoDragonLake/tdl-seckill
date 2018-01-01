/*
 * Copyright (C) 2019 The TwoDragonLake Open Source Project
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

import com.twodragonlake.seckill.constant.CommonConstant;
import com.twodragonlake.seckill.model.Goods;
import com.twodragonlake.seckill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 商品redis库存量.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2016/4/12 11:34
 */
@Component
public class GoodsRedisStoreCache {

    @Autowired
    private RedisUtil redisUtil;

    public void doInit(Goods goods) {
        String key = getKey(goods.getRandomName());
        // 如果没有初始化库存则初始化
        if (!redisUtil.exists(key)) {
            // TODO 这里考虑可以把redis库存设定大一点，这样即使用户占位成功，下单也要及时
            redisUtil.setRedisData(key, goods.getStore());
        }

    }

    private String getKey(String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.REDIS_GOODS_STORE, goodsRandomName);
    }

    /**
     * 减redis库存
     *
     * @param goodsRandomName goodsRandomName
     * @return boolean
     */
    public boolean subtractStore(String goodsRandomName) {
        String key = getKey(goodsRandomName);
        // 减redis库存
        if (redisUtil.decr(key) >= 0) {
            // 如果减成功
            return true;
        } else {
            redisUtil.incr(key);
            return false;
        }
    }

    /**
     * 加redis库存
     *
     * @param goodsRandomName goodsRandomName
     */
    public void plusStore(String goodsRandomName) {
        redisUtil.incr(getKey(goodsRandomName));
    }
}
