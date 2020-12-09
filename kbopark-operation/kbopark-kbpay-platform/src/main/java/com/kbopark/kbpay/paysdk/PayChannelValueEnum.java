package com.kbopark.kbpay.paysdk;

import com.kbopark.operation.enums.MerchantBusinessStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 系统支付通道枚举值
 * @author laomst
 */
@AllArgsConstructor
@Getter
public enum PayChannelValueEnum {
	CHINA_UMS("银联","CHINA-UMS", "银联商务"), // 停车场的自身机构
	CHINA_H5("银联H5支付","CHINA-HPAY", "银联商务H5支付"),
	;

	public final String name;
	public final String code;
	public final String description;

	// 通过code获取一个状态,
	public static PayChannelValueEnum codeOf(String code) {
		return Arrays.stream(PayChannelValueEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(null);
	}

}
