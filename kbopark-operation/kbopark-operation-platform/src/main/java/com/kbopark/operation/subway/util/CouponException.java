package com.kbopark.operation.subway.util;

import cn.hutool.core.exceptions.ExceptionUtil;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/12 17:00
 **/
public class CouponException extends RuntimeException{

	private static final long serialVersionUID = 8068509879445395353L;

	public CouponException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public CouponException(String message) {
		super(message);
	}


}
