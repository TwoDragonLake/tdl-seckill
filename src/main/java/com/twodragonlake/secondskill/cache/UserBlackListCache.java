package com.twodragonlake.secondskill.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twodragonlake.secondskill.constant.CommonConstant;
import com.twodragonlake.secondskill.util.RedisUtil;

/**
 * 黑名单缓存
 * 
 * @category 黑名单缓存
 * @author xiangyong.ding@weimob.com
 * @since 2017年4月18日 下午10:06:21
 */
@Component
public class UserBlackListCache
{
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 增加进入黑名单
	 * 
	 * @category 增加进入黑名单
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年4月18日 下午10:08:42
	 * @param mobile
	 */
	public void addInto(String mobile)
	{
		redisUtil.hset(CommonConstant.RedisKey.USER_BLACK_LIST, mobile, "");
	}

	/**
	 * 是否在黑名单中
	 * 
	 * @category 是否在黑名单中
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年4月18日 下午10:10:51
	 * @param mobile
	 * @return
	 */
	public boolean isIn(String mobile)
	{
		return redisUtil.hget(CommonConstant.RedisKey.USER_BLACK_LIST, mobile, String.class) != null;
	}
}
