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
public enum CouponTypeEnum {

	COUPON_TYPE("coupon","优惠券"),
	RED_PACK_TYPE("redpack","红包"),
	SUBWAY_TICKET_TYPE("ticket","乘车券"),
	;

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
