package com.kbopark.operation.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 状态大于50的属于可用状态
 */
@Getter
@AllArgsConstructor
public enum MerchantBusinessStatusEnum {

	UP(
			"UP",
			1,
			"营业中"
	),
	DOWN(
			"DOWN",
			2,
			"停业中"
	),
	;

	public final String value;
	public final Integer code;
	public final String description;

	// 通过code获取一个状态,
	public static MerchantBusinessStatusEnum codeOf(Integer code) {
		return Arrays.stream(MerchantBusinessStatusEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(DOWN);
	}
}
