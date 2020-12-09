package com.kbopark.operation.unionpay.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:	商户信息查询接口 202006
 * @Date: 2020/9/23 15:04
 **/
@Data
public class TradeDetailDTO {

	/**
     * 企业用户号
	 */
	private String merNo;

	/**
	 * 交易日期，清分流水交易日期
	 * 格式：yyyyMMdd
	 */
	private String transDate;

	/**
	 * 查询项，传固定值：A，
	 */
	private String queryItem;

	/**
	 * 查询值，商户订单号，清分流水商户方唯一标识号
	 */
	private String queryValue;

}
