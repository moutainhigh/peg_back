package com.kbopark.operation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/28 9:23
 **/
@AllArgsConstructor
public enum PackageTypeEnum {

	//Tree_id=1 并且 isPackage=1 是打包套餐，tree_id=1 是一般的套餐
	NO(0,"不打包"),
	YES(1,"打包"),
	;


	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String description;

}
