package com.twodragonlake.secondskill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.twodragonlake.secondskill.BaseTest;
import com.twodragonlake.secondskill.dao.GoodsMapper;
import com.twodragonlake.secondskill.util.RedisUtil;

public class GoodsServiceTest extends BaseTest
{
	@Autowired
	private GoodsService goodsService;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private GoodsMapper goodsMapper;

	private static final int TOTAL_OPERATIONS = 100000;

	@Test
	public void doMiaosha0()
	{
		// goodsService.miaoshaSql("18052101389", 1);
	}

	@Test
	public void reduceStoreAndCreateOrder()
	{
		Assert.assertNotNull(goodsService.reduceStoreAndCreateOrder("12052101390", 1));
	}

	/*
	 * @Test public void doMiaosha() throws InterruptedException,
	 * ExecutionException { int threadCount = 1000; ExecutorService
	 * fixedThreadPool = Executors.newFixedThreadPool(threadCount);
	 * List<Future<Boolean>> results = new ArrayList<Future<Boolean>>(); long
	 * startTime = System.currentTimeMillis(); for (int i = 0; i < threadCount;
	 * i++) { Future<Boolean> itemResult = fixedThreadPool.submit(new
	 * MiaoshaCaller(1)); results.add(itemResult); } fixedThreadPool.shutdown();
	 * while (!fixedThreadPool.isTerminated()) { Thread.sleep(10); }
	 * System.out.println("耗时：" + (System.currentTimeMillis() - startTime)); int
	 * miaoshaResult = 0; // 抢购结果检查 for (Future<Boolean> itemResult : results) {
	 * if (itemResult.get()) { miaoshaResult++; } } System.out.println("秒杀结果：" +
	 * miaoshaResult); }
	 */

	@Test
	public void redis()
	{
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++)
		{
			redisUtil.get("ARTICLE_STORE_BY_ID_1_limiter", Integer.class);
		}

		System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
	}

	/*
	 * class MiaoshaCaller implements Callable<Boolean> { private Integer
	 * miaoShaGoodsId;
	 * 
	 * public MiaoshaCaller(Integer miaoShaGoodsId) { this.miaoShaGoodsId =
	 * miaoShaGoodsId; }
	 * 
	 * @Override public Boolean call() throws Exception { return
	 * goodsService.doMiaosha(miaoShaGoodsId); }
	 * 
	 * }
	 */

	/**
	 * mysql update性能测试
	 * 
	 * @category mysql update性能测试
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年4月20日 上午12:05:28
	 * @throws InterruptedException
	 */
	@Test
	public void mysqlUpdateBenchMark() throws InterruptedException
	{
		long startTime = System.currentTimeMillis();

		List<Thread> tds = new ArrayList<Thread>();
		final AtomicInteger ind = new AtomicInteger();
		for (int i = 0; i < 50; i++)
		{
			Thread hj = new Thread(new Runnable()
			{
				public void run()
				{
					for (int i = 0; (i = ind.getAndIncrement()) < TOTAL_OPERATIONS;)
					{
						goodsMapper.reduceStore(1);
					}
				}
			});
			tds.add(hj);
			hj.start();
		}

		for (Thread t : tds)
			t.join();

		long elapsed = System.currentTimeMillis() - startTime;
		System.out.println(((1000 * TOTAL_OPERATIONS) / elapsed) + " ops");
	}
}
