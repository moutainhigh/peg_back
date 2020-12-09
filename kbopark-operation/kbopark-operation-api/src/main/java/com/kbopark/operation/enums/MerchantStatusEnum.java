package com.kbopark.operation.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 状态大于50的属于可用状态
 */
@Getter
@AllArgsConstructor
public enum MerchantStatusEnum {

	UP(
			"UP",
			1,
			"使用中"
	),
	DOWN(
			"DOWN",
			2,
			"使用中"
	),
	;

	public final String value;
	public final Integer code;
	public final String description;

	// 通过code获取一个状态,
	public static MerchantStatusEnum codeOf(Integer code) {
		return Arrays.stream(MerchantStatusEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(DOWN);
	}
}
