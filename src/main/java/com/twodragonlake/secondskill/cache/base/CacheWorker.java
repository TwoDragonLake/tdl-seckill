package com.twodragonlake.secondskill.cache.base;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.twodragonlake.secondskill.util.RedisUtil;

/**
 * JSON对象缓存工作器模板 dingxiangyong 2016年6月28日 下午6:07:38
 */
public abstract class CacheWorker<P, R>
{
	private static Log logger = LogFactory.getLog(CacheWorker.class);

	@Autowired
	protected RedisUtil redisUtil;

	/**
	 * get方式获取缓存
	 * 
	 * @param params 查询参数
	 * @param expireSeconds 缓存过期时间
	 * @return 结果
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public R get(P params, Class<R> clazz)
	{
		// 获取key，由继承者拼接
		String key = getKey(params);
		Object res = getCache(key, clazz);

		// 如果缓存中存在，直接返回
		if (res != null)
		{
			if (logger.isDebugEnabled())
			{
				StringBuilder sb = new StringBuilder();
				sb.append("从redis获取数据 (key:{").append(key).append("})");

				logger.debug(sb.toString());
			}
			return (R) res;
		}

		if (logger.isDebugEnabled())
		{
			StringBuilder sb = new StringBuilder();
			sb.append("从redis获取数据失败(key:{").append(key).append("}), 准备从DB获取.");

			logger.debug(sb.toString());
		}

		// 否则去DB中取
		R dataFromDb = getDataWhenNoCache(params);
		// 回写cache
		if (dataFromDb != null)
		{
			setCache(getExpireSeconds(), key, dataFromDb);
		}

		return dataFromDb;
	}

	/**
	 * 获取过期时间
	 * 
	 * @return
	 */
	protected abstract int getExpireSeconds();

	/**
	 * set操作 设定缓存
	 * 
	 * @param expireSeconds
	 * @param key
	 * @param dataFromDb
	 */
	protected void setCache(int expireSeconds, String key, R dataFromDb)
	{
		redisUtil.set(key, dataFromDb, expireSeconds);
	}

	/**
	 * set操作 从缓存中取值
	 * 
	 * @param key
	 * @return
	 */
	protected Object getCache(String key, Class<R> clazz)
	{
		// 尝试获取缓存值
		return redisUtil.get(key, clazz);
	}

	public void del(P params)
	{
		// 获取key，由继承者拼接
		String key = getKey(params);
		redisUtil.delete(key);
	}

	/**
	 * 当获取不到缓存时，使用该方法去DB或其他途径取数据
	 * 
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected abstract R getDataWhenNoCache(P params);

	/**
	 * 获取key
	 * 
	 * @param params
	 * @return
	 */
	protected abstract String getKey(P params);

}
