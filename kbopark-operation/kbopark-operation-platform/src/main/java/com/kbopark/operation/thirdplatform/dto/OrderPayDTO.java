package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 15:25
 **/
@Data
public class OrderPayDTO extends RequestPublic {

	/**
	 * 订单号
	 */
	private String orderId;

}
