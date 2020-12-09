package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:批量获取产品状态请求参数
 * @Date: 2020/10/26 14:16
 **/
@Data
public class ProductSpecDTO extends RequestPublic{


	/**
     * 产品号
	 */
	@NotNull(message = "产品编号不能为空")
	private String productNo;

	/**
	 * 开始日期，时间格式：2011-01-01
	 */
	private String travelDate;

	/**
	 * 结束日期
	 */
	private String endTravelDate;

	/**
	 * 规格 ID
	 */
	private String specId;


}
