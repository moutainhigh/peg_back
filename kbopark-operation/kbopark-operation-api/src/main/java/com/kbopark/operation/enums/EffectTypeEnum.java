package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/29 13:42
 **/

@AllArgsConstructor
public enum EffectTypeEnum {

	BY_DAY(1,"按领取日后N天"),
	FIXED_TIME(2,"按固定日期"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
