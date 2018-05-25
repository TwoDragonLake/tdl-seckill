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

package com.twodragonlake.seckill.constant;

/**
 * 常量.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/3/16 12:02
 */
public class CommonConstant {

    public interface RedisKey {
        String GOODS_STORE_BY_ID = "GOODS_STORE_BY_GOODS_ID_{0}";

        String GOODS_INFO_BY_ID = "GOODS_INFO_BY_GOODS_ID_{0}";

        // IP黑名单
        String IP_BLACK_LIST = "IP_BLACK_LIST";

        // 用户黑名单
        String USER_BLACK_LIST = "USER_BLACK_LIST";

        // 秒杀处理列表
        String SECKILL_HANDLE_LIST = "SECKILL_HANDLE_LIST_GOODS_RANDOM_NAME:{0}";

        // redis库存
        String REDIS_GOODS_STORE = "REDIS_GOODS_STORE_GOODS_RANDOM_NAME:{0}";

        // redis占位成功下单token
        String SECKILL_SUCCESS_TOKEN = "SECKILL_SUCCESS_TOKEN_MOBILE:{0}_GOODS_RANDOM_NAME:{1}_";

        // redis占位成功下单token前缀
        String SECKILL_SUCCESS_TOKEN_PREFIX = "SECKILL_SUCCESS_TOKEN_MOBILE";

        // 秒杀结束标志
        String SECKILL_FINISH_FLAG = "SECKILL_FINISH_FLAG_GOODS_RANDOM_NAME:{0}";

    }

    public interface RedisKeyExpireSeconds {
        int GOODS_STORE_BY_ID = 3 * 24 * 60 * 60;
    }

    /**
     * 限流倍数
     */
    public interface CurrentLimitMultiple {
        // 商品购买限流倍数
        int GOODS_BUY = 1;
    }

    /**
     * cookie id
     */
    public static final String COOKIE_NAME = "SECKILL_ID";

    /**
     * token有效期，单位：毫秒
     */
    public static final long TOKEN_EFECTIVE_MILLISECONDS = 3 * 60 * 1000;
}
