package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/29 13:37
 **/
@AllArgsConstructor
public enum PlatformAuditStatusEnum {

	B_WAIT(0,"待审核"),
	B_FAIL(1,"平台方业务人员审核驳回"),
	B_SUCCESS(2,"平台方业务人员审核通过"),
	F_FAIL(3,"平台方财务人员审核驳回"),
	F_SUCCESS(4,"平台方财务人员审核通过"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
