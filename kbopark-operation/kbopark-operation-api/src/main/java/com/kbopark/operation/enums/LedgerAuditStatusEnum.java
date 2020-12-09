package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum LedgerAuditStatusEnum {
	WAIT("WAIT","待审核"),
	SUCCESS("SUCCESS","审核通过"),
	AUDITING("AUDITING","审核中"),
	FAILED("FAILED","审核驳回"),
	;
	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
