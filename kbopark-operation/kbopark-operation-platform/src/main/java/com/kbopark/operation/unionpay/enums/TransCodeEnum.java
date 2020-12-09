package com.kbopark.operation.unionpay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/23 14:55
 **/
@AllArgsConstructor
public enum TransCodeEnum {

	T202001("202001","按流水划付"),
	T202002("202002","按金额划付"),
	T202003("202003","按流水分账"),
	T202004("202004","按金额分账"),
	T202006("202006","商户信息查询"),
	T202007("202007","交易明细查询"),
	T202008("202008","操作记录查询"),
	;
	@Getter
	private String code;

	@Getter
	private String name;

}
