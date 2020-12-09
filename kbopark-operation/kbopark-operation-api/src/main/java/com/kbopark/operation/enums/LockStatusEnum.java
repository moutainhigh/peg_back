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
public enum LockStatusEnum {

	LOCKED("1","已锁定，不可用"),
	UNLOCK("0","可用"),
	;

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String description;

}
