package com.kbopark.kbpay.paysdk;


import com.kbopark.operation.enums.MerchantReviewStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 状态大于50的属于可用状态
 */
@Getter
@AllArgsConstructor
public enum PayTradeOrderPayStatusEnum {

	UN_PAY("UN_PAY", 0, "未支付"),
	TO_PAY("TO_PAY", 1, "待支付"),
	PAID("PAYED", 2, "支付成功");

	public final String status;
	public final Integer code;
	public final String description;

	public static PayTradeOrderPayStatusEnum codeOf(Integer code) {
		return Arrays.stream(PayTradeOrderPayStatusEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(UN_PAY);
	}
}
