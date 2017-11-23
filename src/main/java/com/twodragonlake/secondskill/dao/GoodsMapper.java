package com.twodragonlake.secondskill.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twodragonlake.secondskill.model.Goods;

public interface GoodsMapper
{
	int deleteByPrimaryKey(Integer id);

	int insert(Goods record);

	int insertSelective(Goods record);

	Goods selectByPrimaryKey(Integer id);

	Goods selectByRandomName(String randomName);

	int updateByPrimaryKeySelective(Goods record);

	int updateByPrimaryKey(Goods record);

	/**
	 * 减库存
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月15日 下午5:03:58
	 * @param record
	 * @return
	 */
	int reduceStore(@Param("goodsId") Integer goodsId);

	/**
	 * 根据主键ID查库存
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月15日 下午5:05:46
	 * @param id
	 * @return
	 */
	Integer selectStoreByPrimaryKey(Integer id);

	List<Goods> selectAll();

	/**
	 * 减库存+生成订单
	 * 
	 * @category @author xiangyong.ding@weimob.com
	 * @since 2017年3月15日 下午5:03:58
	 * @param record
	 * @return
	 */
	void doOrder(Map<String, Object> paramMap);
}