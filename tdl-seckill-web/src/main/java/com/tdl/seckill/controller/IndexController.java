package com.tdl.seckill.controller;


import com.tdl.seckill.biz.SeckillOrderBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2018/10/14 13:09
 */

@Controller
@RequestMapping(value = "/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    SeckillOrderBiz seckillOrderBiz;

    @RequestMapping("/subStockAndPushToMQ/{goodId}/{userId}")
    @ResponseBody
    public Integer subStockAndPushToMQ(@PathVariable Long goodId, @PathVariable Long userId){
        int updateCount = 0;
        try {
            updateCount =  seckillOrderBiz.subStockAndPushToMQ(goodId,userId);
        } catch (Exception e) {
            logger.error(" IndexController subStockAndPushToMQ {} ", e);
        }
        return updateCount;
    }

}
