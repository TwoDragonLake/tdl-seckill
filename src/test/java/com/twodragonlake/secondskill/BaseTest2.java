package com.twodragonlake.secondskill;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * junit BaseTest
 * 
 * @author dingxiangyong
 * @time 2016年6月28日 下午2:34:48
 */
@RunWith(SpringJUnit4ClassRunner.class) // 整合
@ContextConfiguration(locations = "classpath*:conf/spring-*.xml") // 加载配置
public class BaseTest2
{
	static
	{
		try
		{
			Log4jConfigurer.initLogging("classpath:log4j.xml");
		}
		catch (FileNotFoundException ex)
		{
			System.err.println("Cannot Initialize log4j");
		}
	}

	@Test
	public void empty()
	{
	}
}
