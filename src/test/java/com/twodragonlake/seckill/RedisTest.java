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

import com.twodragonlake.seckill.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RedisTest.
 *
 * @author : Jerry xu
 * @version : 1.0
 * @since : 2017/12/31 14:29
 */
public class RedisTest {

    private static final int TOTAL_OPERATIONS = 100000;

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        // doTest();
        rPush();
        long elapsed = System.currentTimeMillis() - t;
        System.out.println(((1000 * TOTAL_OPERATIONS) / elapsed) + " ops");
    }

    private static void doTest() throws Exception {
        final RedisUtil redisUtil = new RedisUtil();
        redisUtil.setMinIdle(0);
        redisUtil.setMaxIdle(5);
        redisUtil.setMaxTotal(5);
        redisUtil.setHoled(3000);
        redisUtil.setTimeout(2000);

        redisUtil.setHost("127.0.0.1");
        redisUtil.setPort(6379);
        redisUtil.setPassword("weimob123");
        redisUtil.setDB(0);

        redisUtil.afterPropertiesSet();
        List<Thread> tds = new ArrayList<Thread>();

        final Integer temp = 0;
        final AtomicInteger ind = new AtomicInteger();
        for (int i = 0; i < 50; i++) {
            Thread hj = new Thread(new Runnable() {
                public void run() {
                    for (int i; (i = ind.getAndIncrement()) < TOTAL_OPERATIONS; ) {

                        // Jedis j = pool.getResource();
                        final String key = "foo" + i;

                        // redisUtil.setRedisData(key, key);
                        redisUtil.setRedisData(key, temp);
                    }
                }
            });
            tds.add(hj);
            hj.start();
        }

        for (Thread t : tds)
            t.join();

    }

    private static void rPush() throws Exception {
        final RedisUtil redisUtil = new RedisUtil();
        redisUtil.setMinIdle(0);
        redisUtil.setMaxIdle(5);
        redisUtil.setMaxTotal(5);
        redisUtil.setHoled(3000);
        redisUtil.setTimeout(2000);

        redisUtil.setHost("127.0.0.1");
        redisUtil.setPort(6379);
        redisUtil.setPassword("weimob123");
        redisUtil.setDB(0);

        redisUtil.afterPropertiesSet();

        RedisTest.UserRequestRecord record = new RedisTest.UserRequestRecord();
        record.setMobile("1301387677");
        record.setTimestamp(System.currentTimeMillis());
        redisUtil.rPush("test1", record);
    }

    /**
     * 用户访问记录
     */
    public static class UserRequestRecord {

        /**
         * 手机号，唯一标志用户身份
         */
        private String mobile;

        /**
         * 时间戳
         */
        private long timestamp;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "UserRequestRecord [mobile=" +
                    mobile +
                    ", timestamp=" +
                    timestamp +
                    "]";
        }

    }
}
