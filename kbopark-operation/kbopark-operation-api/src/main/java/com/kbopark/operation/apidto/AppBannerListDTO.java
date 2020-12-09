package com.kbopark.operation.apidto;

import com.kbopark.operation.entity.AppBanner;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AppBannerListDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("轮播图列表")
	private List<AppBanner> bannerList;

	@ApiModelProperty("权益图列表")
	private List<AppBanner> rightImgList;
}
