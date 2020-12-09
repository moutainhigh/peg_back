package com.kbopark.operation.thirdplatform.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author:sunwenzhi
 * @Description:商品信息详情
 * @Date: 2020/10/26 13:31
 **/
@Data
public class ProductInfo{

	/**
	 *产品编号产品唯一标识
	 */
	private Integer productNo;
	/**
	 *产品名称
	 */
	private String productName;
	/**
	 *图片地址
	 */
	private String img;
	/**
	 *是否必须在线支付 0 不是 1 是
	 */
	private Integer isOnlinepay;
	/**
	 * 0 表示线下支付 1 表示在线支付
	 */
	private Integer isPay;
	/**
	 * 0 表示系统自动确认订单 1 表示人工确认订单
	 */
	private Integer isConfirm;
	/**
	 * 不需要配送 需要配送
	 */
	private Integer isExpress;
	/***
	 * 已售张数
	 */
	private Integer ticketCount;
	/**
	 * 销售价格
	 */
	private Double salePrice;
	/**
	 *产品所在城市
	 */
	private String cityName;
	/**
	 *在线支付立减的价格
	 */
	private Double cutPrice;
	/**
	 *市场价
	 */
	private Double marketPrice;
	/**
	 *配送情况
	 */
	private String express;
	/**
	 *预定情况说明
	 */
	private String orderDesc;
	/**
	 *产品销售开始时间
	 */
	private String priceStartDate;
	/**
	 *产品销售结束时间
	 */
	private String priceEndDate;
	/**
	 *景区名称
	 */
	private String viewName;
	/**
	 *景区经度
	 */
	private String viewLongitude;
	/**
	 *景区纬度
	 */
	private String viewLatitude;
	/**
	 *景区地址
	 */
	private String viewAddress;
	/**
	 *景区唯一标识
	 */
	private String viewId;
	/**
	 *产品类型（0 门票，1 套餐或打包产品，2 自由行，3 线路，12 预售）
	 */
	private Integer treeId;
	/**
	 *是否打包产品，0 单独产品，1 打包产品
	 */
	private Integer isPackage;
	/**
	 *出票（发码，发货）操作 0.需要 ,1.不需要
	 */
	private Integer isCode;
	/**
	 *推荐值
	 */
	private String isTop;
	/**
	 *发布或者归档时间
	 */
	private String pubDate;
	/**
	 *标志图
	 */
	private String thumbnail;
	/**
	 *分销价
	 */
	private Double SettlementPrice;
	/**
	 *treeId=12 时有此节点（预售开始时间）
	 */
	private String sale_start_date;
	/**
	 *treeId=12 时有此节点（预售结束时间）
	 */
	private String sale_end_date;
	/**
	 *关注数量
	 */
	private Integer attentCount;

	private Boolean shouldAddress;

	public void setShouldAddress(Boolean shouldAddress) {
		if ("0".equals(this.treeId))
			this.shouldAddress = Boolean.TRUE;
		else
			this.shouldAddress = Boolean.FALSE;
	}
}
