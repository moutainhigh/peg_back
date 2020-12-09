package com.kbopark.operation.unionpay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author:sunwenzhi
 * @Description:	按金额分账接口 202004
 * @Date: 2020/9/23 15:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneySubAccountDTO {

	/**
     * 企业用户号
	 */
	@NotNull(message = "请设置企业用户号")
	@Size(min = 1, message = "请设置企业用户号")
	private String merNo;

	/**
	 * 卡号（明文传入）
	 */
	@NotNull(message = "请设置分账卡号")
	@Size(min = 1, message = "请设置分账卡号")
	private String cardNo;

	/**
	 * 分账附言,最长30个字符，对附言获取byte数组，编码UTF-8，转Base64
	 */
	private String ps;

	/**
	 * 分账金额,实际交易金额,单位分
	 */
	private String payAmt;


}
