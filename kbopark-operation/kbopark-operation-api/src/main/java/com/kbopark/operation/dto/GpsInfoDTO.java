package com.kbopark.operation.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:sunwenzhi
 * @Description:	计算经纬度请求参数
 * @Date: 2020/10/19 11:34
 **/
@Data
public class GpsInfoDTO {

	@NotNull(message = "当前经度不能为空")
	private Double currentLng;
	@NotNull(message = "当前纬度不能为空")
	private Double currentLat;
	@NotNull(message = "目标经度不能为空")
	private Double targetLng;
	@NotNull(message = "目标纬度不能为空")
	private Double targetLat;

}
