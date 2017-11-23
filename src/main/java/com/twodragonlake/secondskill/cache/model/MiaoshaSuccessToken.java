package com.twodragonlake.secondskill.cache.model;

/**
 * 秒杀成功token（用来下单做验证）
 * 
 * @category 秒杀成功token
 * @author xiangyong.ding@weimob.com
 * @since 2017年4月9日 下午12:25:32
 */
public class MiaoshaSuccessToken
{
	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 商品ID
	 */
	private Integer goodsId;

	/**
	 * 成功占redis库存时间
	 */
	private long time;

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public Integer getGoodsId()
	{
		return goodsId;
	}

	public void setGoodsId(Integer goodsId)
	{
		this.goodsId = goodsId;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MiaoshaSuccessToken [mobile=");
		builder.append(mobile);
		builder.append(", goodsId=");
		builder.append(goodsId);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}

}
