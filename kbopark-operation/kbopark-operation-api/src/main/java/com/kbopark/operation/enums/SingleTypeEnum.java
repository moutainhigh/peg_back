package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/29 13:37
 **/
@AllArgsConstructor
public enum SingleTypeEnum {

	NO(0,"单人"),
	YES(1,"多人"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
