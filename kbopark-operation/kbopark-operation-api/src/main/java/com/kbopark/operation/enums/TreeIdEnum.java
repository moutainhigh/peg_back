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
public enum TreeIdEnum {

	TICKET_SINGLE(0,"单一门票"),
	TICKET_PACKAGE(1,"套餐或联票"),
	PHYSICAL_PRODUCT(11,"实物商品"),
	PRE_SELL(12,"预售"),
	OTHER(999,"其他"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
