package com.kbopark.kbpay.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 10:55
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundParam extends PublicParam{
	private static final long serialVersionUID = 1L;


	/**
	 * 要退货的金额
	 */
	private String refundAmount;

	/**
	 * 退货说明
	 */
	private String refundDesc;

}
