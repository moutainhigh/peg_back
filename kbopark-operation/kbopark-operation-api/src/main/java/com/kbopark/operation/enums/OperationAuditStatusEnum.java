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
public enum OperationAuditStatusEnum {

	B_WAIT(0,"待审核"),
	B_FAIL(1,"运营方业务人员审核驳回"),
	B_SUCCESS(2,"运营方业务人员审核通过"),
	F_FAIL(3,"运营方财务人员审核驳回"),
	F_SUCCESS(4,"运营方财务人员审核通过"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
