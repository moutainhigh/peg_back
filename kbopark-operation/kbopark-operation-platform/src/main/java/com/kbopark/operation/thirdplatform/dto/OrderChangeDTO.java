package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:订单退改
 * @Date: 2020/10/27 15:25
 **/
@Data
public class OrderChangeDTO extends RequestPublic {

	/**
	 * 订单号.根据订单号操作订单，如果该订单已经付款，才使用该接口；
	 */
	private String orderId;

	/**
	 * 退改原因
	 */
	private String changeMemo;

	/**
	 *申请退款数量
	 */
	private Integer refundNum;
}
