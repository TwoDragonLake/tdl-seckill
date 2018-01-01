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

package com.twodragonlake.seckill.cache.model;

/**
 * 秒杀成功token（用来下单做验证）.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/4/09 00:25
 */
public class SeckillSuccessToken {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 成功占redis库存时间
     */
    private long time;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SeckillSuccessToken [mobile=" +
                mobile +
                ", goodsId=" +
                goodsId +
                ", time=" +
                time +
                "]";
    }

}
