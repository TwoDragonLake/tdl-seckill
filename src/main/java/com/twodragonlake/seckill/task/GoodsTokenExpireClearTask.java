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

package com.twodragonlake.seckill.task;

import com.twodragonlake.seckill.cache.SeckillSuccessTokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 商品token过期清理任务，过期的token自动释放redis库存.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/4/9 11:47
 */
@Component
public class GoodsTokenExpireClearTask {

    @Autowired
    private SeckillSuccessTokenCache seckillSuccessTokenCache;

    /**
     * 每隔1分钟触发一次
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void doClear() {
        Set<String> keys = seckillSuccessTokenCache.getAllToken();
        for (String key : keys) {
            //验证token是否过期，过期了自动释放redis库存
            seckillSuccessTokenCache.validateTokenByKey(key);
        }
    }


}
