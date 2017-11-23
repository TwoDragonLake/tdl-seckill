package com.twodragonlake.secondskill.cache;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twodragonlake.secondskill.cache.base.CacheWorker;
import com.twodragonlake.secondskill.constant.CommonConstant;
import com.twodragonlake.secondskill.dao.GoodsMapper;
import com.twodragonlake.secondskill.model.Goods;

/**
 * 获取商品信息缓存工作器
 * 
 * @author dingxiangyong 2016年8月26日 上午11:17:38
 */
@Component
public class GoodsInfoCacheWorker extends CacheWorker<Integer, Goods>
{
	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	protected Goods getDataWhenNoCache(Integer goodsId)
	{
		return goodsMapper.selectByPrimaryKey(goodsId);
	}

	@Override
	protected String getKey(Integer goodsId)
	{
		String key = MessageFormat.format(CommonConstant.RedisKey.GOODS_INFO_BY_ID, new Object[] { goodsId });
		return key;
	}

	@Override
	protected int getExpireSeconds()
	{
		return CommonConstant.RedisKeyExpireSeconds.GOODS_STORE_BY_ID;
	}

}
