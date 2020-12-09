package com.kbopark.operation.unionpay.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description: 按流水分账接口 202003
 * @Date: 2020/9/23 15:04
 **/
@Data
public class OrderSubAccountDTO {

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


	/**
	 * 卡号，做Hash运算，算法SHA-256
	 */
	private String cardNo;


	/**
	 * 分账附言,最长30个字符，对附言获取byte数组，编码UTF-8，转Base64
	 */
	private String ps;


}
