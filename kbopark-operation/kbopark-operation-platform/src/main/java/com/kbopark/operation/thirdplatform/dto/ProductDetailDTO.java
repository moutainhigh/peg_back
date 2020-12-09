package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author:sunwenzhi
 * @Description:商品列表请求参数
 * @Date: 2020/10/26 14:16
 **/
@Data
public class ProductDetailDTO extends RequestPublic{

	/**
	 * 产品号
	 */
	@NotNull(message = "产品号不能为空")
	@Size(min = 1, message = "产品号不能为空")
	private String productNo;


}
