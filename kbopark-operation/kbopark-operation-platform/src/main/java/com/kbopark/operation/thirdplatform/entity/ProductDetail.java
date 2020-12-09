package com.kbopark.operation.thirdplatform.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:门票,套餐产品详细,目前问题：第三方文档字段不全
 * @Date: 2020/10/26 16:03
 **/
@Data
public class ProductDetail {

	private Integer productNo;
	private String productName;
	private String ticketTypeName;
	private Integer peopleNum;
	private Integer childrenNum;
	private String img;
	private String imgs;
	private Integer isOnlinepay;
	private Integer isSingle;
	private String is_confirm;
	private String startDate;
	private Integer treeId;
	/**
	 *出票（发码，发货）操作 0.需要 ,1.不需要
	 */
	private Integer isCode;
	/**
	 *是否打包产品，0 单独产品，1 打包产品
	 */
	private Integer isPackage;
	private String viewName;
	private String viewAddress;
	private String cityName;
	private String cityId;
	private String viewId;
	private String ticketCount;
	private String attentCount;
	private Double SettlementPrice;
	private Double salePrice;
	private Double marketPrice;
	private Integer isExpress;
	private Double expressPrice;
	private Integer isFree;
	private Integer startMinute;
	private String startTime;
	private Integer startDay;
	private Integer startNum;
	private Integer maxNum;
	private Integer limitType;
	private Integer cancelDay;
	private Integer validityType;
	private String validityCon;
	private String distributionDesc;
	private String orderDesc;
	private String businessHours;
	private String peoples;
	private String topReason;
	private String priceStartDate;
	private String priceEndDate;
	private String orderPolicy;
	private String content;
	private String custField;
	private String creditTypeSet;
	private String custFieldEx;
	private String state;
	private String charge_include;
	private String refund_note;
	private String user_note;
	private String important_note;

	/**
	 *推荐值
	 */
	private String isTop;
	private String payMinute;


	/**
	 * 产品是否有增值项目
	 */
	private Integer isConds;
	/**
	 * 增值项目列表
	 */
	private List<Cond> conds;

	/**
	 * 退款规则列表
	 */
	private List<Rule> cancelRule;

	/***
	 * >>>>>>>>>>>>>>>>>>>打包套餐产品特殊详细信息
	 */
	private String packageProducts;
	private String packageProduct;
	private String infoIdNode;
	private String infoNameNode;
	private String numNode;
	private String treeIdNode;
	private String isDateNode;
	private String viewIdNode;
	private String viewNameNode;
	private String viewAddressNode;
	/***
	 * <<<<<<<<<<<<<<<<<<<打包套餐产品特殊详细信息
	 */

	/***
	 * >>>>>>>>>>>>>>>>>>预售产品特殊详细信息
	 */
	private String appointment;
	private String posterImg;
	private String sale_start_date;
	private String sale_end_date;
	/***
	 * <<<<<<<<<<<<<<<<<<预售产品特殊详细信息
	 */


	/***
	 * >>>>>>>>>>>>>>>>>4.商品产品特殊详细信息
	 */
	private String prod_desc;
	private String goodsunit;
	/***
	 * <<<<<<<<<<<<<<<<<<商品产品特殊详细信息
	 */

}
