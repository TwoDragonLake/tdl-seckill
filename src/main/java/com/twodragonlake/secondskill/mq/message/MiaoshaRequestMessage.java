package com.twodragonlake.secondskill.mq.message;

import java.io.Serializable;

/**
 * 秒杀请求消息
 * 
 * @category 秒杀请求消息
 * @author xiangyong.ding@weimob.com
 * @since 2017年4月7日 下午5:34:13
 */
public class MiaoshaRequestMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5810025604361901986L;

	/**
	 * 手机号，标识用户唯一身份
	 */
	private String mobile;

	/**
	 * 秒杀商品编号
	 */
	private String goodsRandomName;

	public MiaoshaRequestMessage()
	{
		super();
	}

	public MiaoshaRequestMessage(String mobile, String goodsRandomName)
	{
		super();
		this.mobile = mobile;
		this.goodsRandomName = goodsRandomName;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getGoodsRandomName()
	{
		return goodsRandomName;
	}

	public void setGoodsRandomName(String goodsRandomName)
	{
		this.goodsRandomName = goodsRandomName;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("MiaoshaRequestMessage [mobile=");
		builder.append(mobile);
		builder.append(", goodsRandomName=");
		builder.append(goodsRandomName);
		builder.append("]");
		return builder.toString();
	}

}
