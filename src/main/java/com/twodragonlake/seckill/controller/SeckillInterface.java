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

package com.twodragonlake.seckill.controller;

import com.twodragonlake.mvc.annotation.RequestMapping;
import com.twodragonlake.mvc.enums.ReturnType;
import com.twodragonlake.mvc.interceptor.annotation.Intercept;
import com.twodragonlake.seckill.cache.SeckillSuccessTokenCache;
import com.twodragonlake.seckill.intercept.UserInterceptor;
import com.twodragonlake.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口路由.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/1/23 9:32
 */
@Controller
@RequestMapping(value = "/i/")
public class SeckillInterface {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillSuccessTokenCache seckillSuccessTokenCache;

    /**
     * 秒杀接口
     *
     * @param mobile          mobile
     * @param goodsRandomName goodsRandomName
     * @return String
     */
    @Intercept(value = {UserInterceptor.class})
    // @Intercept(value = { ExecuteTimeInterceptor.class })
    @RequestMapping(value = "seckill", returnType = ReturnType.JSON)
    public String seckill(String mobile, String goodsRandomName) {
        Assert.notNull(goodsRandomName);
        Assert.notNull(mobile);

        goodsService.Seckill(mobile, goodsRandomName);

        // 为什么要返回mobile，为了方便jmeter测试
        return mobile;
    }

    /**
     * 获取秒杀商品的链接
     */
    @RequestMapping(value = "{goodsId}/getSeckillGoodsLink", returnType = ReturnType.JSON)
    public String getSeckillGoodsLink(Integer goodsId) {
        return goodsService.getGoodsRandomName(goodsId);
    }

    /**
     * 查询是否秒杀成功
     */
    @RequestMapping(value = "seckillResult", returnType = ReturnType.JSON)
    public String isSeckillSuccess(String mobile, String goodsRandomName) {
        // 直接取缓存查询是否有成功的记录生成
        return seckillSuccessTokenCache.queryToken(mobile, goodsRandomName);
    }

    /**
     * 下单
     */
    @RequestMapping(value = "order", returnType = ReturnType.JSON)
    public Integer order(String mobile, Integer goodsId, String token) {
        return goodsService.order(mobile, goodsId, token);
    }

    /**
     * 查询系统时间
     */
    @RequestMapping(value = "time/now", returnType = ReturnType.JSON)
    @ResponseBody
    public Long time() {
        return System.currentTimeMillis();
    }
}
