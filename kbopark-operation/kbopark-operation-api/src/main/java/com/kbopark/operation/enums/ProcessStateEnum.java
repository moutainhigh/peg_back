package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:退款状态
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum ProcessStateEnum {

	APPLY("APPLY","提交"),
	AUDITING("AUDITING","审核中"),
	REFUNDED("REFUNDED","已退款"),
	FAIL("FAIL","已拒绝");

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
