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

import com.twodragonlake.seckill.cache.GoodsRedisStoreCache;
import com.twodragonlake.seckill.dao.GoodsMapper;
import com.twodragonlake.seckill.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品redis库存初始化任务.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/4/9 11:47
 */
@Component
public class GoodsRedisStoreInitTask {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsRedisStoreCache goodsRedisStore;

    /**
     * 每隔1分钟触发一次
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void doInit() {
        List<Goods> goods = goodsMapper.selectAll();
        for (Goods item : goods) {
            goodsRedisStore.doInit(item);

        }
    }

}
