package com.kbopark.operation.subway.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/29 13:42
 **/

@AllArgsConstructor
public enum ResponseMsgEnum {

	ERROR("00","系统异常"),
	SUCCESS("01","成功"),
	FAIL("02","失败"),
	;

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String msg;

}
