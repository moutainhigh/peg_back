package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 14:03
 **/
@Data
public class OrderDetailDTO extends RequestPublic{

	/**
	 * 订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderId;

	/**
	 * 分销商订单号
	 */
	private String orderSourceId;

}
