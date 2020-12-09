package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/29 13:42
 **/
@AllArgsConstructor
public enum RedpackMiniTypeEnum {

	RANDOM(1,"随机"),
	PLAIN(2,"普通"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

	// 通过code获取一个状态,
	public static RedpackMiniTypeEnum codeOf(Integer code) {
		return Arrays.stream(RedpackMiniTypeEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(PLAIN);
	}


}
