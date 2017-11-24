package com.twodragonlake.secondskill.common.exception;

/**
 * 业务异常
 * 
 * @author dingxiangyong 2016年7月13日 下午9:37:45
 */
public class BusinessException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3992962807763740333L;

	/**
	 * 错误码
	 */
	private String errorCode;

	/**
	 * 错误信息
	 */
	private String errorInfo;

	public BusinessException()
	{
		super();
	}

	public BusinessException(String errorCode, String errorInfo)
	{
		super();
		this.errorCode = errorCode;
		this.errorInfo = errorInfo;
	}

	public BusinessException(String errorInfo)
	{
		this.errorInfo = errorInfo;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorInfo()
	{
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo)
	{
		this.errorInfo = errorInfo;
	}

}
