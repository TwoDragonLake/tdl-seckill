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

package com.twodragonlake.seckill.mq.message;

import java.io.Serializable;

/**
 * 秒杀请求消息.
 *
 * @author : ceaserwang
 * @version : 1.0
 * @since : 2017/4/7 17:34
 */
public class SeckillRequestMessage implements Serializable {

    private static final long serialVersionUID = 5810025604361901986L;

    /**
     * 手机号，标识用户唯一身份
     */
    private String mobile;

    /**
     * 秒杀商品编号
     */
    private String goodsRandomName;

    public SeckillRequestMessage() {
        super();
    }

    public SeckillRequestMessage(String mobile, String goodsRandomName) {
        super();
        this.mobile = mobile;
        this.goodsRandomName = goodsRandomName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGoodsRandomName() {
        return goodsRandomName;
    }

    public void setGoodsRandomName(String goodsRandomName) {
        this.goodsRandomName = goodsRandomName;
    }

    @Override
    public String toString() {
        return "SeckillRequestMessage [mobile=" +
                mobile +
                ", goodsRandomName=" +
                goodsRandomName +
                "]";
    }

}
