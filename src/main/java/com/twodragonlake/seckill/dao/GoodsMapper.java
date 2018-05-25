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

package com.twodragonlake.seckill.dao;

import com.twodragonlake.seckill.model.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品映射.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/1/23 9:32
 */
public interface GoodsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    Goods selectByRandomName(String randomName);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    /**
     * 减库存
     */
    int reduceStore(@Param("goodsId") Integer goodsId);

    /**
     * 根据主键ID查库存
     */
    Integer selectStoreByPrimaryKey(Integer id);

    List<Goods> selectAll();

    /**
     * 减库存+生成订单
     */
    void doOrder(Map<String, Object> paramMap);
}