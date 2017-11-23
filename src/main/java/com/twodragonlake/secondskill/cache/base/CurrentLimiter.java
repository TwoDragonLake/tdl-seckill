package com.twodragonlake.secondskill.cache.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.twodragonlake.secondskill.util.RedisUtil;

/**
 * redis限流器
 * 
 * @category @author xiangyong.ding@weimob.com
 * @since 2017年3月16日 下午12:05:19
 */
public abstract class CurrentLimiter<P>
{
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 做限流，如果超过了流量则抛出异常
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月16日 下午1:51:38
	 * @param id
	 * @param errorMsg
	 */
	public void doLimit(P param, String errorMsg)
	{
		// 获取流量最大值
		int limit = getLimit(param);

		// 现有流量值
		Integer currentLimit = getCurrentLimit();

		// 如果现有流量值大于了限流值，或者自增了流量之后大于了限流值则表示操作收到了限流
		if (currentLimit != null && currentLimit >= limit)
		{
			throw new RuntimeException(errorMsg);
		}
	}

	/**
	 * 获取即时流量值
	 */
	protected abstract int getCurrentLimit();

	/**
	 * 获取限流器名字
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月16日 下午1:38:24
	 * @return
	 */
	protected abstract String getLimiterName(P param);

	/**
	 * 获取限流的最大流量
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月16日 下午1:39:17
	 * @return
	 */
	protected abstract int getLimit(P param);

}
