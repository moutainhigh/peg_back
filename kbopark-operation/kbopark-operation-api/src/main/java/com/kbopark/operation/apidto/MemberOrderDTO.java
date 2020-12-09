package com.kbopark.operation.apidto;

import com.kbopark.operation.entity.CondValue;
import com.kbopark.operation.entity.Detail;
import com.kbopark.operation.entity.People;
import com.kbopark.operation.entity.Prod;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/27 11:03
 **/
@Data
public class MemberOrderDTO {

	/**
	 * 使用优惠券则设置优惠券编号
	 */
	private String couponSerialNumber;

	/**
	 * 商品编号
	 */
	@NotNull(message = "商品编号不能为空")
	private String productNo;
	/**
	 * 商品名称
	 */
	@NotNull(message = "商品名称不能为空")
	private String productName;
	/**
	 * 应付金额
	 */
	@NotNull(message = "应付金额不能为空")
	private Double payable;

	/**
	 * 游玩时间
	 */
	private String travelTime;
	/**
	 * 商品规格
	 */
	private String specType;
	/**
	 * 购买数量
	 */
	@NotNull(message = "购买数量不能为空")
	private Integer buyNum;

	/**
	 * 配送地址ID
	 */
	//TODO 现行版本去除地址
//	@NotNull(message = "配送地址不能为空")
	private Integer takeAddressId;

	/**
	 * 证件号
	 */
	private String linkCreditNo;

	/**
	 * 邮箱
	 */
	private String linkEmail;



	/**
	 * 订单产品类别,tree_id=11 和 tree_id=12 的实体商品规格节点需要传商品规格价格ID
	 */
	@NotNull(message = "产品类别不能为空")
	private Integer treeId;

	/**
	 * 是否有增值项，值为”0”,无增值
	 */
	@NotNull(message = "是否有增值项不能为空")
	private Integer isConds;

	/**
	 * 是否是单人，值为”1”,则购买多少张票需要填写多少个客人信息
	 */
	@NotNull(message = "是否单人不能为空")
	private Integer isSingle;

	/**
	 * 打包产品,只有 treeId=1 并且 isPackage=1 时必填
	 */
	@NotNull(message = "是否打包不能为空")
	private Integer isPackage;

	/**
	 * 下单需要填写得字段
	 */
	@NotNull(message = "custField不能为空")
	private String custField;

	/**
	 * 其他联系人节点
	 */
	private List<People> peoples;

	/**
	 * 打包产品子产品节点
	 */
	private List<Prod> prods;

	/**
	 * 非多规格此节点<details>去掉,多规格需要设置
	 */
	private List<Detail> details;

	/**
	 * 增值项目
	 */
	private List<CondValue> conds;

}
