package com.kbopark.operation.dto;

import com.kbopark.operation.entity.FeedbackInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 9:17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class FeedBackDTO extends FeedbackInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 反馈人姓名
	 */
	@ApiModelProperty(value = "反馈人姓名")
	private String name;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;
	/**
	 * 反馈信息详情
	 */
	@ApiModelProperty(value = "反馈信息详情")
	@NotNull(message = "请设置反馈信息")
	@Size(min = 1, message = "请设置反馈信息")
	private String detailInfo;


}
