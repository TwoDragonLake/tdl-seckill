package com.twodragonlake.secondskill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twodragonlake.secondskill.cache.MiaoshaSuccessTokenCache;
import com.twodragonlake.secondskill.intercept.UserInterceptor;
import com.twodragonlake.secondskill.service.GoodsService;

import twodragonlake.twodragonlakemvc.framework.annotation.RequestMapping;
import twodragonlake.twodragonlakemvc.framework.enums.ReturnType;
import twodragonlake.twodragonlakemvc.framework.interceptor.annotation.Intercept;

/**
 * 接口路由
 * 
 * @category 接口路由
 * @author xiangyong.ding@weimob.com
 * @since 2017年1月23日 下午9:32:49
 */
@Controller
@RequestMapping(value = "/i/")
public class MiaoshaInterface
{
	@Autowired
	private GoodsService goodsService;

	@Autowired
	private MiaoshaSuccessTokenCache miaoshaSuccessTokenCache;

	/**
	 * 秒杀接口
	 * 
	 * @category 秒杀接口
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年5月2日 下午11:23:23
	 * @param mobile
	 * @param goodsRandomName
	 * @return
	 */
	@Intercept(value = { UserInterceptor.class })
	// @Intercept(value = { ExecuteTimeInterceptor.class })
	@RequestMapping(value = "miaosha", returnType = ReturnType.JSON)
	public String miaosha(String mobile, String goodsRandomName)
	{
		Assert.notNull(goodsRandomName);
		Assert.notNull(mobile);

		goodsService.miaosha(mobile, goodsRandomName);

		// 为什么要返回mobile，为了方便jmeter测试
		return mobile;
	}

	/**
	 * 获取秒杀商品的链接
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年4月24日 下午12:47:40
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "{goodsId}/getMiaoshaGoodsLink", returnType = ReturnType.JSON)
	public String getMiaoshaGoodsLink(Integer goodsId)
	{
		return goodsService.getGoodsRandomName(goodsId);
	}

	/**
	 * 查询是否秒杀成功
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年4月12日 下午10:55:32
	 * @param mobile
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "miaoshaResult", returnType = ReturnType.JSON)
	public String isMiaoshaSuccess(String mobile, String goodsRandomName)
	{
		// 直接取缓存查询是否有成功的记录生成
		return miaoshaSuccessTokenCache.queryToken(mobile, goodsRandomName);
	}

	/**
	 * 下单
	 * 
	 * @category 下单
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年4月25日 上午12:35:34
	 * @param mobile
	 * @param goodsId
	 * @param token
	 */
	@RequestMapping(value = "order", returnType = ReturnType.JSON)
	public Integer order(String mobile, Integer goodsId, String token)
	{
		return goodsService.order(mobile, goodsId, token);
	}

	/**
	 * 查询系统时间
	 * 
	 * @category 查询系统时间
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年5月1日 下午10:24:58
	 * @return
	 */
	@RequestMapping(value = "time/now", returnType = ReturnType.JSON)
	@ResponseBody
	public Long time()
	{
		return System.currentTimeMillis();
	}
}
