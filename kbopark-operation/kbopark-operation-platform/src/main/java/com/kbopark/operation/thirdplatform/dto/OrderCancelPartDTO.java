package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:订单部分取消
 * @Date: 2020/10/27 15:25
 **/
@Data
public class OrderCancelPartDTO extends RequestPublic {

	/**
	 * 订单号.根据订单号操作订单，订单必须为已支付状态,取消人数不能大于订单人数；
	 */
	private String orderId;

	/**
	 * 取消人数
	 */
	private Integer num;
}
