package com.twodragonlake.secondskill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig
{
	/**
	 * 检测恶意IP，多少秒被出现多少次请求
	 */
	@Value("${ip_black_times}")
	private int ipBlackTimes;

	/**
	 * 检测恶意IP，多少秒被出现多少次请求
	 */
	@Value("${ip_black_seconds}")
	private int ipBlackSeconds;

	/**
	 * 检测恶意用户，多少秒被出现多少次请求
	 */
	@Value("${user_black_times}")
	private int userBlackTimes;

	/**
	 * 检测恶意用户，多少秒被出现多少次请求
	 */
	@Value("${user_black_seconds}")
	private int userBlackSeconds;

	public int getIpBlackTimes()
	{
		return ipBlackTimes;
	}

	public void setIpBlackTimes(int ipBlackTimes)
	{
		this.ipBlackTimes = ipBlackTimes;
	}

	public int getIpBlackSeconds()
	{
		return ipBlackSeconds;
	}

	public void setIpBlackSeconds(int ipBlackSeconds)
	{
		this.ipBlackSeconds = ipBlackSeconds;
	}

	public int getUserBlackTimes()
	{
		return userBlackTimes;
	}

	public void setUserBlackTimes(int userBlackTimes)
	{
		this.userBlackTimes = userBlackTimes;
	}

	public int getUserBlackSeconds()
	{
		return userBlackSeconds;
	}

	public void setUserBlackSeconds(int userBlackSeconds)
	{
		this.userBlackSeconds = userBlackSeconds;
	}

}
