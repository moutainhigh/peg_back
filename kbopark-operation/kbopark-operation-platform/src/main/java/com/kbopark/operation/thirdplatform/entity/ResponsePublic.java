package com.kbopark.operation.thirdplatform.entity;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:返回公共参数
 * @Date: 2020/10/26 13:41
 **/
@Data
public class ResponsePublic {

	/**
	 * 状态0 失败 1 成功
	 */
	private Integer status;

	/**
	 * 返回业务消息提示
	 */
	private String msg;

	/**
	 * 搜索结果产品总数
	 */
	private Integer totalNum;

}
