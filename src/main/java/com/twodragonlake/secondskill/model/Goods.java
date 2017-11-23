package com.twodragonlake.secondskill.model;

import java.util.Date;

public class Goods
{
	private Integer id;

	private String name;

	/**
	 * 随机名称，该名称只有在抢购开始后才生成
	 */
	private String randomName;

	private Integer store;

	private Integer version;

	private Date startTime;

	private Date endTime;

	private Boolean delFlag;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getStore()
	{
		return store;
	}

	public void setStore(Integer store)
	{
		this.store = store;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public Boolean getDelFlag()
	{
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag)
	{
		this.delFlag = delFlag;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public String getRandomName()
	{
		return randomName;
	}

	public void setRandomName(String randomName)
	{
		this.randomName = randomName;
	}

}