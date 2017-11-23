package com.twodragonlake.secondskill.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twodragonlake.secondskill.constant.CommonConstant;
import com.twodragonlake.secondskill.util.RedisUtil;

/**
 * 秒杀正在处理请求列表
 * 
 * @category 秒杀正在处理请求列表
 * @author xiangyong.ding@weimob.com
 * @since 2017年4月20日 上午12:01:16
 */
@Component
public class MiaoshaHandlingListCache
{
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 增加到处理列表
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年4月19日 下午11:59:35
	 * @param mobile
	 * @param goodsRandomName
	 */
	public void add2HanleList(String mobile, String goodsRandomName)
	{
		redisUtil.hset(getKey(goodsRandomName), mobile, mobile);
	}

	/**
	 * 增加到处理列表
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年4月19日 下午11:59:35
	 * @param mobile
	 * @param goodsRandomName
	 */
	public void removeFromHanleList(String mobile, String goodsRandomName)
	{
		redisUtil.hdel(getKey(goodsRandomName), mobile);
	}

	/**
	 * 是否在正在处理列表中
	 * 
	 * @category 是否在正在处理列表中
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年4月19日 下午11:59:35
	 * @param mobile
	 * @param goodsRandomName
	 */
	public boolean isInHanleList(String mobile, String goodsRandomName)
	{
		return redisUtil.hget(getKey(goodsRandomName), mobile, String.class) != null;
	}

	private String getKey(String goodsRandomName)
	{
		return CommonConstant.RedisKey.MIAOSHA_HANDLE_LIST + goodsRandomName;
	}

}
