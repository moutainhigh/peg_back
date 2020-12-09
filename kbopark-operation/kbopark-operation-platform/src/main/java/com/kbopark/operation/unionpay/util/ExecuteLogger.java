package com.kbopark.operation.unionpay.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kbopark.operation.unionpay.dto.SuperDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/24 17:39
 **/
@Data
public class ExecuteLogger {

	/**
	 * 交易码
	 */
	@ApiModelProperty(value = "交易码")
	private String transCode;
	/**
	 * 交易名称
	 */
	@ApiModelProperty(value = "交易名称")
	private String transName;
	/**
	 * 请求系统流水号
	 */
	@ApiModelProperty(value = "请求系统流水号")
	private String srcReqId;
	/**
	 * 请求参数
	 */
	@ApiModelProperty(value = "请求参数")
	private String requestParam;
	/**
	 * 响应数据
	 */
	@ApiModelProperty(value = "响应数据")
	private String responseData;
	/**
	 * 验签结果
	 */
	@ApiModelProperty(value = "验签结果")
	private String validResult;


	/**
	 * 返回数据
	 */
	@ApiModelProperty(value = "返回数据")
	@JsonIgnore
	private SuperDto superDto;
	
	
}
