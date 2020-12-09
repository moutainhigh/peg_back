package com.kbopark.operation.dto;

import com.kbopark.operation.entity.LedgerCheck;
import lombok.Data;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/19 16:38
 **/
@Data
public class LedgerCheckDTO extends LedgerCheck {

	/**
	 * 分账订单ids
	 */
	private List<Long> orderIds;
	/**
	 * 商家ID
	 */
	private Integer merchantId;
	/***
	 * 结算日期范围
	 */
	private List<String> dateTime;

}
