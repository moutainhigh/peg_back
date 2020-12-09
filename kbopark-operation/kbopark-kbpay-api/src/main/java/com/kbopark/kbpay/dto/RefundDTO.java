package com.kbopark.kbpay.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 10:25
 **/
@Data
public class RefundDTO {

	@ApiModelProperty("订单流水号")
	private String tradeBizNumber;

	/**
	 * 退款金额
	 */
	@ApiModelProperty("退款金额")
	private Double totalPrice;

}
