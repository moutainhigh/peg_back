package com.kbopark.operation.unionpay.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:	按流水划付接口 202001
 * @Date: 2020/9/23 15:04
 **/
@Data
public class OrderPaymentDTO {

	/**
	 * 企业用户号
	 */
	private String merNo;

	/**
	 * 商户订单号
	 */
	private String merOrderNo;

	/**
	 * 分账金额,实际交易金额,单位分
	 */
	private String payAmt;


}
