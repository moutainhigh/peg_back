package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:	财务审核策略
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum LedgerStrategyEnum {

	ARTI("arti","人工审核"),
	AUTO("auto","自动审核"),
	;
	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String name;

}
