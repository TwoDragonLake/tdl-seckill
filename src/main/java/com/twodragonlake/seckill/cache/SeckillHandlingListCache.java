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

/**
 * 秒杀正在处理请求列表.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/4/20 12:01
 */
@Component
public class SeckillHandlingListCache {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 增加到处理列表
     */
    public void add2HandleList(String mobile, String goodsRandomName) {
        redisUtil.setRedisData(getKey(goodsRandomName), mobile, mobile);
    }

    /**
     * 增加到处理列表
     */
    public void removeFromHandleList(String mobile, String goodsRandomName) {
        redisUtil.setRedisData(getKey(goodsRandomName), mobile);
    }

    /**
     * 是否在正在处理列表中
     */
    public boolean isInHandleList(String mobile, String goodsRandomName) {
        return redisUtil.getRedisData(getKey(goodsRandomName), mobile, String.class) != null;
    }

    private String getKey(String goodsRandomName) {
        return CommonConstant.RedisKey.SECKILL_HANDLE_LIST + goodsRandomName;
    }

}
