package com.kbopark.operation.thirdplatform.dto;

import lombok.Data;

/**
 * @Author:sunwenzhi
 * @Description:商品列表请求参数
 * @Date: 2020/10/26 14:16
 **/
@Data
public class ProductListDTO extends RequestPublic{


	/**
	 * 关键字,系统自动切词
	 */
	private String keyWrod;

	/**
	 * 产品类型 0 单一门票 1 套餐或联票
	 */
	private Integer treeId;

	/**
	 * 地区名称
	 */
	private String cityName;

	/**
	 * 支付方式 0 表示线下支付 1 表示在线支付
	 */
	private Integer isPay;

	/**
	 * 确认方式 0 表示系统自动确认订单 1 表示人工确认订单
	 */
	private Integer isConfirm;

	/**
	 * 配送方式
	 */
	private Integer isExpress;

	/**
	 * 景点账户,分销系统系统提供的景点账 户（获取参见景区接口）
	 */
	private String viewId;

	/**
	 * 景点标签,分销系统系统提供的景点标 签编号(获取参见景区标签接 口) 支持多个标签查询用“,”分 隔
	 */
	private Integer tagIds;

	/**
	 * 位置信息,格式: (经度，纬度，方圆距离) 默认距离 2 公里
	 */
	private String location;

	/**
	 * 一页显示多少 条,默认 50 条，最多可设置 100 条
	 */
	private Integer pageNum;

	/**
	 *页数,默认第一页
	 */
	private Integer pageNo;

	/**
	 * 排序方式 0 按价格 1 按折扣 2 按销量 3 按推荐值 4 上架时间
	 */
	private String orderBy;

	/**
	 * 是否打包产品，0 单独产品， 1 打包产品
	 * （注 isPackage 只能传 0 或者 1，不是直接查询 打包产品不用填，打包产品 treeId=1）
	 */
	private Integer isPackage;

}
