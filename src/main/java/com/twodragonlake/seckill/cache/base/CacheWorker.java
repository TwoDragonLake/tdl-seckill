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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * JSON对象缓存工作器模板.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2016/6/28 18:07
 */
public abstract class CacheWorker<P, R> {

    private static Log logger = LogFactory.getLog(CacheWorker.class);

    @Autowired
    protected RedisUtil redisUtil;

    /**
     * get方式获取缓存
     *
     * @param params 查询参数
     * @param clazz  Class
     * @return R
     */
    @SuppressWarnings("unchecked")
    public R get(P params, Class<R> clazz) {
        // 获取key，由继承者拼接
        String key = getKey(params);
        Object res = getCache(key, clazz);

        // 如果缓存中存在，直接返回
        if (res != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("从redis获取数据 (key:{" + key + "})");
            }
            return (R) res;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("从redis获取数据失败(key:{" + key + "}), 准备从DB获取.");
        }

        // 否则去DB中取
        R dataFromDb = getDataWhenNoCache(params);
        // 回写cache
        if (dataFromDb != null) {
            setCache(getExpireSeconds(), key, dataFromDb);
        }

        return dataFromDb;
    }

    /**
     * 获取过期时间
     */
    protected abstract int getExpireSeconds();

    /**
     * set操作 设定缓存
     *
     * @param expireSeconds expireSeconds
     * @param key           key
     * @param dataFromDb    dataFromDb
     */
    private void setCache(int expireSeconds, String key, R dataFromDb) {
        redisUtil.setRedisData(key, dataFromDb, expireSeconds);
    }

    /**
     * get操作 从缓存中取值
     *
     * @param key   key
     * @param clazz Class
     * @return Object
     */
    private Object getCache(String key, Class<R> clazz) {
        // 尝试获取缓存值
        return redisUtil.getRedisData(key, clazz);
    }

    public void del(P params) {
        // 获取key，由继承者拼接
        String key = getKey(params);
        redisUtil.deleteRedisDataByKey(key);
    }

    /**
     * 当获取不到缓存时，使用该方法去DB或其他途径取数据
     *
     * @param params P
     * @return R
     */
    protected abstract R getDataWhenNoCache(P params);

    /**
     * 获取key
     *
     * @param params P
     * @return String
     */
    protected abstract String getKey(P params);

}
