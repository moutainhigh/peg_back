package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:批量获取订单状态
 * @Date: 2020/10/27 15:25
 **/
@Data
public class OrderStatusDTO extends RequestPublic {

	/**
	 * 支持多个订单号查 询，“，”分割
	 */
	private String orderId;

}
