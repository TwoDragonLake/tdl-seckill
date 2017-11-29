package com.twodragonlake.secondskill;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.twodragonlake.secondskill.mq.message.MiaoshaRequestMessage;
import com.twodragonlake.secondskill.util.RedisUtil;

public class RedisUtilTest extends BaseTest
{
	@Autowired
	private RedisUtil redisUtil;

	@Test
	public void getAndSet()
	{
		redisUtil.set("test_key", "test get and set!!!", 60);
		System.out.println(redisUtil.get("test_key", String.class));

		redisUtil.set("test_key", new Integer(10), 60);
		System.out.println(redisUtil.get("test_key", Integer.class));

		MiaoshaRequestMessage message = new MiaoshaRequestMessage("17052101388",
				"0e67e331-c521-406a-b705-64e557c4c06c");

		redisUtil.set("test_key", message, 60);
		System.out.println(redisUtil.get("test_key", MiaoshaRequestMessage.class));
	}
}
