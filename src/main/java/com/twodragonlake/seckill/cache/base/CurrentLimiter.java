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

package com.twodragonlake.seckill.cache.base;

import com.twodragonlake.seckill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * redis限流器.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2016/3/16 00:05
 */
public abstract class CurrentLimiter<P> {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 做限流，如果超过了流量则抛出异常
     *
     * @param param    P
     * @param errorMsg errorMsg
     */
    public void doLimit(P param, String errorMsg) {
        // 获取流量最大值
        int limit = getLimit(param);

        // 现有流量值
        Integer currentLimit = getCurrentLimit();

        // 如果现有流量值大于了限流值，或者自增了流量之后大于了限流值则表示操作收到了限流
        if (currentLimit >= limit) {
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * 获取即时流量值
     */
    protected abstract int getCurrentLimit();

    /**
     * 获取限流器名字
     */
    protected abstract String getLimiterName(P param);

    /**
     * 获取限流的最大流量
     */
    protected abstract int getLimit(P param);

}
