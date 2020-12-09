package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:订单类型
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum OrderTypeEnum {

	SCAN_CODE("SCAN_CODE","扫码订单"),
	QXD_OD("QXD_OD","青小岛订单");

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
