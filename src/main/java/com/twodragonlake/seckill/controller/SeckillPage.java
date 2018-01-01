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
import com.twodragonlake.seckill.model.Goods;
import com.twodragonlake.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/p/")
public class SeckillPage {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "list")
    public String list(Map<String, Object> returnMap) {
        List<Goods> list = goodsService.getGoodsList();
        returnMap.put("list", list);

        return "list";
    }

    @RequestMapping(value = "{goodsId}/detail")
    public String detail(Integer goodsId, Map<String, Object> returnMap) {
        Assert.notNull(goodsId);

        Goods goods = goodsService.getDetail(goodsId);

        Assert.notNull(goods);

        returnMap.put("goods", goods);

        return "detail";

    }
}
