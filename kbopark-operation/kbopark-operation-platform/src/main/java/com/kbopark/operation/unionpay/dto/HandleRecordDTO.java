package com.kbopark.operation.unionpay.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:	商户信息查询接口 202006
 * @Date: 2020/9/23 15:04
 **/
@Data
public class HandleRecordDTO {

	/**
     * 企业用户号
	 */
	private String merNo;

	/**
	 * 请求发起日期
	 */
	private String reqDate;

	/**
	 * 请求交易流水号,
	 * 被查询交易商户端请求流水号（对应202001到202004报文头中srcReqId）
	 */

	private String reqJournalNo;

}
