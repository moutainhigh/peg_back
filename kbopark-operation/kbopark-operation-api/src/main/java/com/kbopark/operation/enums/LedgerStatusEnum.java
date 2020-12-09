package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:	分账状态
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum LedgerStatusEnum {

	SETTLED("SETTLED","已分账"),
	UNSETTLE("UNSETTLE","待分账"),
	FAILED("FAILED","分账失败"),
	NOSETTLE("NOSETTLE","驳回不予分账")
	;
	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
