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
public enum CondTypeEnum {

	NO(0,"无增值项目"),
	YES(1,"有增值项目"),
	;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
