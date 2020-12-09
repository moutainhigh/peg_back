package com.kbopark.operation.unionpay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:sunwenzhi
 * @Description:	按金额划付接口 202002
 * @Date: 2020/9/23 15:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyPaymentDTO {

	/**
     * 企业用户号
	 */
	private String merNo;

	/**
     * 分账金额,实际交易金额,单位分
	 */
	private String payAmt;


}
