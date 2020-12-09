package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:批量获取产品状态请求参数
 * @Date: 2020/10/26 14:16
 **/
@Data
public class ProductStatusDTO extends RequestPublic{


	/**
	 * 支持多个产品号查 询，“，”分割
	 */
	@NotNull(message = "产品编号不能为空")
	private String productNo;


}
