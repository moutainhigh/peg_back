package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:	核对账单生成状态
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum LedgerCreateStatusEnum {

	WAIT("WAIT","待生成"),
	CREATED("CREATED","已生成对账单")
	;
	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
