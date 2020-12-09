package com.kbopark.operation.entity;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description: 参数增值项目节点
 * @Date: 2020/10/27 15:14
 **/
@Data
public class CondValue {

	/**
	 * 增值项目 id
	 */
	private Integer cond_id;

	/**
	 * 预定增值数量
	 */
	private Integer num;

}
