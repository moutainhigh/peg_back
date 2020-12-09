package com.kbopark.kbpay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 13:52
 **/
@AllArgsConstructor
public enum MsgTypeEnum {

	TRADE_PAY("trade.h5Pay","H5支付下单"),
	QUERY("query","支付结果查询"),
	REFUND("refund","订单退款申请"),
	REFUND_QUERY("refundQuery","退货查询"),
	CLOSE("close","订单关闭"),
	;

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;



}
