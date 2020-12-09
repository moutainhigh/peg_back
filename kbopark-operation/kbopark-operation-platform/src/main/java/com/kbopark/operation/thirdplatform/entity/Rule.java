package com.kbopark.operation.thirdplatform.entity;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:退款规则详细信息
 * @Date: 2020/10/27 8:54
 **/
@Data
public class Rule {

	/**
	 * 有效退款日期,0 表示当天，负数表示前 x 天，正数表
	 * 示后 x 天
	 */
	private Integer days;
	/**
	 * 有效退款时间,申请退款需要在这个时间之前
	 */
	private String hours;
	/**
	 * 分销商可退款
	 */
	private String cancelMoneyCodex;
	/**
	 * 供应商退回
	 */
	private String returnMoneyCodex;
	/**
	 * 是否支持订单完
	 * 成后做强制退改
	 */
	private String agreeForcetg;
}
