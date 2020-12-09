package cn.qdzhhl.kbopark.admin.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/8/28 8:59
 **/
@AllArgsConstructor
public enum DeptLevelEnum {


	ONE(1,"一级部门"),
	TWO(2,"二级部门"),
	THREE(3,"三级部门"),
	;

	@Getter
	public final Integer code;

	@Getter
	public final String description;

}
