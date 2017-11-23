package com.twodragonlake.secondskill.cache;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twodragonlake.secondskill.model.Goods;
import com.twodragonlake.secondskill.util.RedisUtil;

/**
 * 商品redis库存量
 * 
 * @category 商品redis库存量
 * @author xiangyong.ding@weimob.com
 * @since 2017年4月12日 下午11:34:26
 */
@Component
public class GoodsRedisStoreCache
{
	@Autowired
	private RedisUtil redisUtil;

	public void doInit(Goods goods)
	{
		String key = getKey(goods.getRandomName());
		// 如果没有初始化库存则初始化
		if (!redisUtil.exists(key))
		{
			// TODO 这里考虑可以把redis库存设定大一点，这样即使用户占位成功，下单也要及时
			redisUtil.set(key, goods.getStore() * 1);
		}

	}

	private String getKey(String goodsRandomName)
	{
		String key = MessageFormat.format(com.twodragonlake.secondskill.constant.CommonConstant.RedisKey.REDIS_GOODS_STORE,
				new Object[] { goodsRandomName });
		return key;
	}

	/**
	 * 减redis库存
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年4月9日 下午12:11:47
	 * @param goodsId
	 * @return
	 */
	public boolean decrStore(String goodsRandomName)
	{
		String key = getKey(goodsRandomName);
		// 减redis库存
		if (redisUtil.decr(key) >= 0)
		{
			// 如果减成功
			return true;
		}
		else
		{
			redisUtil.incr(key);
			return false;
		}
	}

	/**
	 * 加redis库存
	 * 
	 * @category 加redis库存
	 * @author xiangyong.ding@weimob.com
	 * @since 2017年4月9日 下午12:11:47
	 * @param goodsId
	 * @return
	 */
	public void incrStore(String goodsRandomName)
	{
		redisUtil.incr(getKey(goodsRandomName));
	}
}
