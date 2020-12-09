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
public enum CouponAuditStatusEnum {

	WAIT(0,"待审核"),
	SUCCESS(1,"审核成功"),
	FAIL(2,"审核驳回"),
	WAIT_PLATFORM(3, "等待平台审核")
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
