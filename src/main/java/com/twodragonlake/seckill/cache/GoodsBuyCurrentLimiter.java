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

import com.twodragonlake.message.MessageMonitor;
import com.twodragonlake.seckill.cache.base.CurrentLimiter;
import com.twodragonlake.seckill.constant.CommonConstant;
import com.twodragonlake.seckill.constant.MessageType;
import com.twodragonlake.seckill.dao.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 商品购买限流器.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/3/16 13:54
 */
@Component
public class GoodsBuyCurrentLimiter extends CurrentLimiter<String> {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MessageMonitor messageMonitor;

    @Override
    protected String getLimiterName(String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.GOODS_STORE_BY_ID, goodsRandomName);
    }

    @Override
    protected int getLimit(String goodsRandomName) {
        return goodsMapper.selectByRandomName(goodsRandomName).getStore()
                * CommonConstant.CurrentLimitMultiple.GOODS_BUY;
    }

    @Override
    protected int getCurrentLimit() {
        return messageMonitor.getMessageLeft(MessageType.SECKILL_MESSAGE);
    }

}
