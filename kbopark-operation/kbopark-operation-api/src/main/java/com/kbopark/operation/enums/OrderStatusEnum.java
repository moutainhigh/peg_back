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
public enum OrderStatusEnum {

	WAIT("WAIT","待支付"),
	SUCCESS("SUCCESS","支付成功"),
	CANCEL("CANCEL","取消支付"),
	REFUNDING("REFUNDING","申请退款中"),
	REFUSED("REFUSED","已拒绝"),
	REFUNDED("REFUNDED","已退款"),
	FAIL("FAIL","支付失败");

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
