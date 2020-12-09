package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:	账户类别
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum BelongTypeEnum {

	MERCHANT("MERCHANT","普通商家"),
	OPERATION("OPERATION","运营商"),
	PLATFORM("PLATFORM","平台"),
	;
	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
