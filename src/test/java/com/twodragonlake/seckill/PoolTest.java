/*
 * Copyright (C) 2015 The TwoDragonLake Open Source Project
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

package com.twodragonlake.seckill;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PoolTest.
 *
 * @author : Jerry xu
 * @version : 1.0
 * @since : 2017/12/31 14:28
 */
public class PoolTest {
    private static final int TOTAL_OPERATIONS = 100000;

    public static void main(String[] args) throws Exception {
        Jedis j = new Jedis("127.0.0.1", 6379);
        j.connect();
        j.auth("weimob123");
        j.flushAll();
        j.quit();
        j.disconnect();
        long t = System.currentTimeMillis();
        // withoutPool();
        withPool();
        long elapsed = System.currentTimeMillis() - t;
        System.out.println(((1000 * TOTAL_OPERATIONS) / elapsed) + " ops");
    }

    private static void withPool() throws Exception {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // poolConfig.setMinIdle(0);
        // poolConfig.setMaxIdle(5);
        // poolConfig.setMaxTotal(5);
        final JedisPool pool = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000, "weimob123", 0);
        List<Thread> tds = new ArrayList<Thread>();

        final AtomicInteger ind = new AtomicInteger();
        for (int i = 0; i < 50; i++) {
            Thread hj = new Thread(new Runnable() {
                public void run() {
                    for (; ind.getAndIncrement() < TOTAL_OPERATIONS; ) {
                        try {
                            Jedis j = pool.getResource();
                            // final String key = "foo" + i;
                            // j.setRedisData(key, key);
                            // j.getRedisData(key);
                            j.get("foo1");
                            j.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            tds.add(hj);
            hj.start();
        }

        for (Thread t : tds)
            t.join();

        pool.destroy();

    }
}
