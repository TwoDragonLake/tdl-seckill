package com.twodragonlake.secondskill.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisUtilBenchmark
{
	private static final int TOTAL_OPERATIONS = 100000;

	public static void main(String[] args) throws Exception
	{
		long t = System.currentTimeMillis();
		// dotest();
		lpush();
		long elapsed = System.currentTimeMillis() - t;
		System.out.println(((1000 * TOTAL_OPERATIONS) / elapsed) + " ops");
	}

	private static void dotest() throws Exception
	{
		final RedisUtil redisUtil = new RedisUtil();
		redisUtil.setMinIdle(0);
		redisUtil.setMaxIdle(5);
		redisUtil.setMaxTotal(5);
		redisUtil.setHoled(3000);
		redisUtil.setTimeout(2000);

		redisUtil.setHost("127.0.0.1");
		redisUtil.setPort(6379);
		redisUtil.setPassword("weimob123");
		redisUtil.setDB(0);

		redisUtil.afterPropertiesSet();
		List<Thread> tds = new ArrayList<Thread>();

		final Integer temp = 0;
		final AtomicInteger ind = new AtomicInteger();
		for (int i = 0; i < 50; i++)
		{
			Thread hj = new Thread(new Runnable()
			{
				public void run()
				{
					for (int i = 0; (i = ind.getAndIncrement()) < TOTAL_OPERATIONS;)
					{

						// Jedis j = pool.getResource();
						final String key = "foo" + i;

						// redisUtil.set(key, key);
						redisUtil.set(key, temp);
					}
				}
			});
			tds.add(hj);
			hj.start();
		}

		for (Thread t : tds)
			t.join();

	}

	private static void lpush() throws Exception
	{
		final RedisUtil redisUtil = new RedisUtil();
		redisUtil.setMinIdle(0);
		redisUtil.setMaxIdle(5);
		redisUtil.setMaxTotal(5);
		redisUtil.setHoled(3000);
		redisUtil.setTimeout(2000);

		redisUtil.setHost("127.0.0.1");
		redisUtil.setPort(6379);
		redisUtil.setPassword("weimob123");
		redisUtil.setDB(0);

		redisUtil.afterPropertiesSet();

		UserRequestRecord record = new UserRequestRecord();
		record.setMobile("1301387677");
		record.setTimestamp(System.currentTimeMillis());
		redisUtil.lpush("test1", record);
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