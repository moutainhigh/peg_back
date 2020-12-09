package com.kbopark.operation.entity;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:多规格打包
 * @Date: 2020/10/27 15:11
 **/
@Data
public class Detail {


	/**
	 * 规格价格 ID
	 */
	private Integer cond;

	/**
	 * 预定数量
	 */
	private Integer num;

}
