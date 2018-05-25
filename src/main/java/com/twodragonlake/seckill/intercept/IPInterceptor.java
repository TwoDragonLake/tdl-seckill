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

package com.twodragonlake.seckill.intercept;

import com.twodragonlake.mvc.exception.BusinessException;
import com.twodragonlake.mvc.interceptor.RequestInterceptor;
import com.twodragonlake.seckill.config.SystemConfig;
import com.twodragonlake.seckill.constant.CommonConstant;
import com.twodragonlake.seckill.util.IPUtil;
import com.twodragonlake.seckill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 恶意IP检测拦截器（暂时未启用，对于企业网络，很有大的误杀可能，谨慎使用）.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/3/26 15:41
 */
@Component
public class IPInterceptor implements RequestInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SystemConfig systemConfig;

    private static final String IP_REQUEST_TIMES_PREFIX = "ip_request_times_";

    // IP地址校验
    private static Pattern pattern = Pattern.compile(
            "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");

    public String description() {
        return "接口时间计算拦截器";
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1.获取真实IP
        String ip = IPUtil.getIp(request);

        // 匹配IP是否是正常IP
        Matcher matcher = pattern.matcher(ip);
        // 2. 验证IP：验证IP格式；验证IP是否在黑名单中
        if (!matcher.find() || redisUtil.getRedisData(CommonConstant.RedisKey.IP_BLACK_LIST, ip, String.class) != null) {
            throw new BusinessException("抢购已经结束啦");
        }

        // 增加该IP的访问次数
        Long requestedTimes = redisUtil.incr(IP_REQUEST_TIMES_PREFIX + ip);

        // 超过限定时间内的访问频率
        if (requestedTimes > systemConfig.getIpBlackTimes()) {
            // 模拟加入IP黑名单，实际应用时这里要优化入库，下次重启服务时重新加载
            redisUtil.setRedisData(CommonConstant.RedisKey.IP_BLACK_LIST, ip, "");
            throw new BusinessException("抢购已经结束啦");
        }

        if (requestedTimes.intValue() == 1) {
            // 如果第一次设定访问次数，则增加过期时间
            redisUtil.expire(IP_REQUEST_TIMES_PREFIX + ip, systemConfig.getIpBlackSeconds());
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object returnObj) throws Exception {

    }

    public void commitHandle(HttpServletRequest request, HttpServletResponse response) {

    }

}
