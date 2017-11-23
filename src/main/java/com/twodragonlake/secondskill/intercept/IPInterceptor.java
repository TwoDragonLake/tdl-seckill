package com.twodragonlake.secondskill.intercept;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twodragonlake.secondskill.config.SystemConfig;
import com.twodragonlake.secondskill.constant.CommonConstant;
import com.twodragonlake.secondskill.util.IPUtil;
import com.twodragonlake.secondskill.util.RedisUtil;

import wang.moshu.smvc.framework.exception.BusinessException;
import wang.moshu.smvc.framework.interceptor.RequestInterceptor;

/**
 * 恶意IP检测拦截器（暂时未启用，对于企业网络，很有大的误杀可能，谨慎使用）
 * 
 * @category @author xiangyong.ding@weimob.com
 * @since 2017年3月26日 下午3:41:21
 */
@Component
public class IPInterceptor implements RequestInterceptor
{
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private SystemConfig systemConfig;

	private static final String IP_REQUEST_TIMES_PREFIX = "ip_request_times_";

	// IP地址校验
	private static Pattern pattern = Pattern.compile(
			"((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");

	public String description()
	{
		return "接口时间计算拦截器";
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 1.获取真实IP
		String ip = IPUtil.getIp((HttpServletRequest) request);

		// 匹配IP是否是正常IP
		Matcher matcher = pattern.matcher(ip);
		// 2. 验证IP：验证IP格式；验证IP是否在黑名单中
		if (!matcher.find() || redisUtil.hget(CommonConstant.RedisKey.IP_BLACK_LIST, ip, String.class) != null)
		{
			throw new BusinessException("抢购已经结束啦");
		}

		// 增加该IP的访问次数
		Long requestedTimes = redisUtil.incr(IP_REQUEST_TIMES_PREFIX + ip);

		// 超过限定时间内的访问频率
		if (requestedTimes > systemConfig.getIpBlackTimes())
		{
			// 模拟加入IP黑名单，实际应用时这里要优化入库，下次重启服务时重新加载
			redisUtil.hset(CommonConstant.RedisKey.IP_BLACK_LIST, ip, "");
			throw new BusinessException("抢购已经结束啦");
		}

		if (requestedTimes.intValue() == 1)
		{
			// 如果第一次设定访问次数，则增加过期时间
			redisUtil.expire(IP_REQUEST_TIMES_PREFIX + ip, systemConfig.getIpBlackSeconds());
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object returnObj) throws Exception
	{

	}

	public void commitHandle(HttpServletRequest request, HttpServletResponse response)
	{

	}

}
