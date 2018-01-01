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
 * 黑名单缓存.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/4/18 10:06
 */
@Component
public class UserBlackListCache {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 增加进入黑名单
     */
    public void addInto(String mobile) {
        redisUtil.setRedisData(CommonConstant.RedisKey.USER_BLACK_LIST, mobile, "");
    }

    /**
     * 是否在黑名单中
     */
    public boolean isIn(String mobile) {
        return redisUtil.getRedisData(CommonConstant.RedisKey.USER_BLACK_LIST, mobile, String.class) != null;
    }
}
