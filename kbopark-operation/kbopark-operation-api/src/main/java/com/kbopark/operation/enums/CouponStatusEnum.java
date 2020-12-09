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
public enum CouponStatusEnum {

	OFFLINE(0,"下架"),
	ONLINE(1,"上架"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
