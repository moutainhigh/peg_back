package com.kbopark.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/8 12:43
 **/
@Data
@ApiModel(value = "用户端获取商家")
public class MerchantInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 所属运营商id
	 */
	@ApiModelProperty(value = "所属运营商id")
	private Integer operatorId;
	/**
	 * 所属渠道商
	 */
	@ApiModelProperty(value = "所属渠道商")
	private Integer distributorId;
	/**
	 * 所属推广员id
	 */
	@ApiModelProperty(value = "所属推广员id")
	private Integer promoterId;

	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String name;


	@ApiModelProperty("商户简称")
	private String simpleName;

	/**
	 * 商家联系人
	 */
	@ApiModelProperty(value = "商家联系人")
	private String linkMan;
	/**
	 * 商家联系电话
	 */
	@ApiModelProperty(value = "商家联系电话")
	private String linkPhone;

	@ApiModelProperty("推广识别码")
	private String promoteCode;
	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String provinceName;
	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String cityName;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String areaName;
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String address;

	@ApiModelProperty("经度")
	private Double lng;

	@ApiModelProperty("纬度")
	private Double lat;

	 @ApiModelProperty(value = "logo")
	private String logo;


	@ApiModelProperty("商家简介")
	private String remark;

	@ApiModelProperty("商品简介")
	private String goodsRemark;

	@ApiModelProperty("商家分类")
	private Integer categoryKey;

	/***商家距离**/
	private Double distance;

}
