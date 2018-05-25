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

import com.twodragonlake.seckill.constant.CommonConstant;
import com.twodragonlake.seckill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 秒杀结束缓存.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/4/18 10:23
 */
@Component
public class SeckillFinishCache {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设定是否秒杀结束
     */
    public void setFinish(String goodsRandomName) {
        redisUtil.setRedisData(getKey(goodsRandomName), "");
    }

    /**
     * 指定商品秒杀是否结束
     */
    public boolean isFinish(String goodsRandomName) {
        return redisUtil.getRedisData(getKey(goodsRandomName), String.class) != null;
    }

    private String getKey(String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.SECKILL_FINISH_FLAG, goodsRandomName);
    }
}
