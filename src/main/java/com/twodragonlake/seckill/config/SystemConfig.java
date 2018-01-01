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

package com.twodragonlake.seckill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统config.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/4/18 10:06
 */
@Component
public class SystemConfig {
    /**
     * 检测恶意IP，多少秒被出现多少次请求
     */
    @Value("${ip_black_times}")
    private int ipBlackTimes;

    /**
     * 检测恶意IP，多少秒被出现多少次请求
     */
    @Value("${ip_black_seconds}")
    private int ipBlackSeconds;

    /**
     * 检测恶意用户，多少秒被出现多少次请求
     */
    @Value("${user_black_times}")
    private int userBlackTimes;

    /**
     * 检测恶意用户，多少秒被出现多少次请求
     */
    @Value("${user_black_seconds}")
    private int userBlackSeconds;

    public int getIpBlackTimes() {
        return ipBlackTimes;
    }

    public void setIpBlackTimes(int ipBlackTimes) {
        this.ipBlackTimes = ipBlackTimes;
    }

    public int getIpBlackSeconds() {
        return ipBlackSeconds;
    }

    public void setIpBlackSeconds(int ipBlackSeconds) {
        this.ipBlackSeconds = ipBlackSeconds;
    }

    public int getUserBlackTimes() {
        return userBlackTimes;
    }

    public void setUserBlackTimes(int userBlackTimes) {
        this.userBlackTimes = userBlackTimes;
    }

    public int getUserBlackSeconds() {
        return userBlackSeconds;
    }

    public void setUserBlackSeconds(int userBlackSeconds) {
        this.userBlackSeconds = userBlackSeconds;
    }

}
