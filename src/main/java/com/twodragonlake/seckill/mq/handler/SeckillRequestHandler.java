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

package com.twodragonlake.seckill.mq.handler;

import com.twodragonlake.message.AbstarctMessageHandler;
import com.twodragonlake.mvc.exception.BusinessException;
import com.twodragonlake.seckill.cache.GoodsRedisStoreCache;
import com.twodragonlake.seckill.cache.SeckillFinishCache;
import com.twodragonlake.seckill.cache.SeckillSuccessTokenCache;
import com.twodragonlake.seckill.cache.UserBlackListCache;
import com.twodragonlake.seckill.constant.MessageType;
import com.twodragonlake.seckill.mq.message.SeckillRequestMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息的处理器.
 *
 * @author : dingxiangyong
 * @version : 1.0
 * @since : 2017/2/3 21:21
 */
@Service
public class SeckillRequestHandler extends AbstarctMessageHandler<SeckillRequestMessage> {

    private static Log logger = LogFactory.getLog(SeckillRequestHandler.class);

    @Autowired
    private GoodsRedisStoreCache goodsRedisStoreCache;

    @Autowired
    private SeckillSuccessTokenCache seckillSuccessTokenCache;

    @Autowired
    private UserBlackListCache userBlackListCache;

    @Autowired
    private SeckillFinishCache seckillFinishCache;

    public SeckillRequestHandler() {
        // 说明该handler监控的消息类型；失败重试次数设定为MAX_VALUE
        super(MessageType.SECKILL_MESSAGE, SeckillRequestMessage.class, Integer.MAX_VALUE);
    }

    /**
     * 监听到消息后处理方法
     */
    public void handle(SeckillRequestMessage message) {
        long startTime = System.currentTimeMillis();
        // 查看请求用户是否在黑名单中
        if (userBlackListCache.isIn(message.getMobile())) {
            logger.error(message.getMobile() + "检测为黑名单用户，拒绝抢购");
            return;
        }
        // logger.error("1耗时：" + (System.currentTimeMillis() - startTime));
        long startTime2 = System.currentTimeMillis();
        // 先看抢购是否已经结束了
        if (seckillFinishCache.isFinish(message.getGoodsRandomName())) {
            logger.error("抱歉，您来晚了，抢购已经结束了");
            return;
        }
        // logger.error("2耗时：" + (System.currentTimeMillis() - startTime));
        long startTime3 = System.currentTimeMillis();
        // 先减redis库存
        if (!goodsRedisStoreCache.subtractStore(message.getGoodsRandomName())) {
            // 减库存失败
            throw new BusinessException("占redis名额失败，等待重试");
        }
        // logger.error("3耗时：" + (System.currentTimeMillis() - startTime));
        long startTime4 = System.currentTimeMillis();
        // 减库存成功：生成下单token，并存入redis供前端获取
        String token = seckillSuccessTokenCache.genToken(message.getMobile(), message.getGoodsRandomName());
        // logger.error("4耗时：" + (System.currentTimeMillis() - startTime));
        long startTime5 = System.currentTimeMillis();
        String sb = "step1:" + (startTime2 - startTime) + "step2:" + (startTime3 - startTime2) +
                "step3:" + (startTime4 - startTime3) + "step4:" + (startTime5 - startTime4) +
                "all:" + (startTime5 - startTime) + ",token:" + token;
        logger.error(sb);

    }

    public void handleFailed(SeckillRequestMessage obj) {
        logger.warn("msg:[" + obj + "], 超过失败次数，停止重试。");

    }

}
