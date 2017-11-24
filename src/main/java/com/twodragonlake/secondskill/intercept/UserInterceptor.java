package com.twodragonlake.secondskill.intercept;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twodragonlake.secondskill.cache.UserBlackListCache;
import com.twodragonlake.secondskill.config.SystemConfig;
import com.twodragonlake.secondskill.util.RedisUtil;

import twodragonlake.twodragonlakemvc.framework.exception.BusinessException;
import twodragonlake.twodragonlakemvc.framework.interceptor.RequestInterceptor;

/**
 * 恶意用户检测拦截器
 * 
 * @category @author xiangyong.ding@weimob.com
 * @since 2017年3月26日 下午3:41:21
 */
@Component
public class UserInterceptor implements RequestInterceptor
{
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private UserBlackListCache userBlackListCache;

	@Autowired
	private SystemConfig systemConfig;

	private static final String USER_REQUEST_TIMES_PREFIX = "user_request_times_";

	// 手机号正则表达式
	// private static Pattern pattern =
	// Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");

	public String description()
	{
		return "恶意用户检测拦截器";
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 1.获取用户手机号
		String mobile = request.getParameter("mobile");

		// 匹配手机号是否是正常手机号
		// Matcher matcher = pattern.matcher(mobile);
		// // 2. 验证用户是否在黑名单中
		// if (!matcher.find() || userBlackListCache.isIn(mobile))
		// {
		// throw new BusinessException("抢购已经结束啦");
		// }

		// 2. 验证用户是否在黑名单中
		if (userBlackListCache.isIn(mobile))
		{
			throw new BusinessException("抢购已经结束啦");
		}

		// 查询该用户访问记录
		List<UserRequestRecord> requestRecords = redisUtil.lrange(USER_REQUEST_TIMES_PREFIX + mobile, 0,
				systemConfig.getIpBlackTimes() - 1, UserRequestRecord.class);

		// 超过限定时间内的访问频率
		if (requestRecords.size() + 1 >= systemConfig.getIpBlackTimes() && (System.currentTimeMillis()
				- requestRecords.get(requestRecords.size() - 1).timestamp < systemConfig.getUserBlackSeconds() * 1000))
		{
			// 模拟加入IP黑名单，实际应用时这里要优化入库，下次重启服务时重新加载
			userBlackListCache.addInto(mobile);

			// 清空访问记录缓存
			redisUtil.delete(USER_REQUEST_TIMES_PREFIX + mobile);
			throw new BusinessException("抢购已经结束啦");
		}

		else
		{
			UserRequestRecord requestRecord = new UserRequestRecord();
			requestRecord.setMobile(mobile);
			requestRecord.setTimestamp(System.currentTimeMillis());
			// 如果第一次设定访问次数，则增加过期时间
			redisUtil.lpush(USER_REQUEST_TIMES_PREFIX + mobile, requestRecord);
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object returnObj) throws Exception
	{

	}

	public void commitHandle(HttpServletRequest request, HttpServletResponse response)
	{

	}

	/**
	 * 用户访问记录
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月30日 下午4:20:47
	 */
	public static class UserRequestRecord
	{
		/**
		 * 手机号，唯一标志用户身份
		 */
		private String mobile;

		/**
		 * 时间戳
		 */
		private long timestamp;

		public String getMobile()
		{
			return mobile;
		}

		public void setMobile(String mobile)
		{
			this.mobile = mobile;
		}

		public long getTimestamp()
		{
			return timestamp;
		}

		public void setTimestamp(long timestamp)
		{
			this.timestamp = timestamp;
		}

		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("UserRequestRecord [mobile=");
			builder.append(mobile);
			builder.append(", timestamp=");
			builder.append(timestamp);
			builder.append("]");
			return builder.toString();
		}

	}
}
