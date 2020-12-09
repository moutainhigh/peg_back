package com.kbopark.operation.vo;

import com.kbopark.operation.entity.CouponInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/8 11:09
 **/
@Data
@ApiModel(value = "用户端优惠券对象")
public class CouponInfoVO extends CouponInfo implements Serializable,Comparable<CouponInfoVO> {
	private static final long serialVersionUID = 1L;


	/***商家名称**/
	private String name;
	/***商家经纬度**/
	private Double lng;
	/***商家经纬度*/
	private Double lat;
	/***商家LOGO**/
	private String logo;
	/***商家联系电话**/
	private String linkPhone;
	/***商家类别**/
	private Integer categoryKey;
	/***商家距离**/
	private Double distance;
	/***商家地址**/
	private String provinceName;
	private String cityName;
	private String areaName;
	private String address;

	/***优惠券、红包列表展示图*/
	private String listLogo;
	/***详情页轮播图，用于红包和优惠券详情轮播*/
	private String banners;
	/***用户是否已经领取*/
	private Boolean userReceived;


	@Override
	public int compareTo(CouponInfoVO o) {
		return this.distance.compareTo(o.distance);
	}
}
