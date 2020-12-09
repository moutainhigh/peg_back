package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 14:23
 **/
@Data
public class OrderSaveDTO extends RequestPublic{

	/**
	 * 上传参数，xml格式
	 */
	private String param;

}
