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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;

/**
 * 秒杀获取到了下单资格token缓存.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/4/13 12:00
 */
@Component
public class SeckillSuccessTokenCache {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoodsRedisStoreCache goodsRedisStoreCache;

    @Autowired
    private SeckillHandlingListCache seckillHandlingListCache;

    public String genToken(String mobile, String goodsRandomName) {
        String key = getKey(mobile, goodsRandomName);
        String token = getToken();
        redisUtil.setRedisData(key + token, System.currentTimeMillis());
        // redisUtil.incr("test_ddd");
        return token;
    }

    /**
     * 查询token
     */
    public String queryToken(String mobile, String goodsRandomName) {
        Set<String> keys = redisUtil.keys(getKey(mobile, goodsRandomName) + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            String key = keys.iterator().next();

            return key.substring(key.lastIndexOf("_") + 1);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 验证token
     */
    public boolean validateToken(String mobile, String goodsRandomName, String token) {
        String key = getKey(mobile, goodsRandomName) + token;
        Long tokenSavedTimeStamp = redisUtil.getRedisData(key, Long.class);

        // 判断token是否过了有效期
        if (tokenSavedTimeStamp != null
                && (System.currentTimeMillis() - tokenSavedTimeStamp < CommonConstant.TOKEN_EFECTIVE_MILLISECONDS)) {
            // 已经验证了的清楚掉
            redisUtil.deleteRedisDataByKey(key);
            // 如果token验证成功
            return true;
        } else if (tokenSavedTimeStamp != null) {
            // 失效了的清楚掉
            redisUtil.deleteRedisDataByKey(key);
            // 如果token存在，且是过期的，则回馈redis库存
            goodsRedisStoreCache.plusStore(goodsRandomName);

            seckillHandlingListCache.removeFromHandleList(mobile, goodsRandomName);
        }

        return false;
    }

    /**
     * 以KEY方式验证token是否失效
     */
    public void validateTokenByKey(String key) {
        Long tokenSavedTimeStamp = redisUtil.getRedisData(key, Long.class);

        // 判断token是否过了有效期
        if (tokenSavedTimeStamp != null
                && (System.currentTimeMillis() - tokenSavedTimeStamp > CommonConstant.TOKEN_EFECTIVE_MILLISECONDS)) {
            // 失效了的清楚掉
            redisUtil.deleteRedisDataByKey(key);
            // 如果token存在，且是过期的，则回馈redis库存
            goodsRedisStoreCache.plusStore(key.substring(key.lastIndexOf(":"), key.lastIndexOf("_")));
        }
    }

    private String getKey(String mobile, String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.SECKILL_SUCCESS_TOKEN, mobile, goodsRandomName);
    }

    public Set<String> getAllToken() {
        return redisUtil.keys(CommonConstant.RedisKey.SECKILL_SUCCESS_TOKEN_PREFIX + "*");
    }

    /**
     * 获取随机名称
     *
     * @return
     */
    public static String getToken() {
        return UUID.randomUUID().toString();
    }
}
