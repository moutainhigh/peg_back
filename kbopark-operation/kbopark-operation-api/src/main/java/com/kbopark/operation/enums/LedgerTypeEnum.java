package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:	分账类别
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum LedgerTypeEnum {

	MERCHANT("merchant","商家"),
	OPERATION("operation","运营商"),
	PLATFORM("platform","平台"),
	;
	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String name;

}
