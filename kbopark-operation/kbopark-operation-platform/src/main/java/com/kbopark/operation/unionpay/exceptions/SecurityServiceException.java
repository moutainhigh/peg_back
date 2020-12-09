package com.kbopark.operation.unionpay.exceptions;

/**
 * 当交易为安全处理异常时 抛出此异常
 * @author shaosijun
 *
 */
public class SecurityServiceException extends RuntimeException{
	private String errCode;
	private String errMsg;
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private static final long serialVersionUID = 6800377423794695777L;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public SecurityServiceException() {
		this("安全处理错");
	}
	
	public SecurityServiceException(String errMsg){
		this("1111111",errMsg);
	}
	
	public SecurityServiceException(String errCode , String errMsg){
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
}
