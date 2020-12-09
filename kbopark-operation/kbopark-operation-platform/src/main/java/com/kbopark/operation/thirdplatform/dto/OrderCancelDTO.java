package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 15:25
 **/
@Data
public class OrderCancelDTO extends RequestPublic {

	/**
	 * 三方订单号,根据订单号操作订单，如果该订单已经付款，则为退款；
	 */
	@NotNull(message = "请设置订单ID")
	private String orderId;

	/**
	 * 本系统订单号
	 */
	@NotNull(message = "请设置订单号")
	private String orderNumber;

}
