package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;


/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/26 14:17
 **/
@Data
public class RequestPublic {

	/**
	 * 分销商帐号
	 */
	private String custId;

	/**
	 * 分销商验证码
	 */
	private String apikey;
}
