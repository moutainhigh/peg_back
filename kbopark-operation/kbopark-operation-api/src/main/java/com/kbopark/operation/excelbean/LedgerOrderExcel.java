package com.kbopark.operation.excelbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/20 17:06
 **/
@Data
public class LedgerOrderExcel {

	/**
	 * 分账批次号
	 */
	@ApiModelProperty(value = "分账批次号")
	private String ledgerBatchNo;
	/**
	 * 分账状态  已结算/未结算
	 */
	@ApiModelProperty(value = "分账状态  已结算/未结算")
	private String ledgerStatus;
	/**
	 * 运营商名称
	 */
	@ApiModelProperty(value = "运营商名称")
	private String operationName;
	/**
	 * 运营商分账比例
	 */
	@ApiModelProperty(value = "运营商分账比例")
	private Double operationPercent;
	/**
	 * 运营商分账金额
	 */
	@ApiModelProperty(value = "运营商分账金额")
	private Double operationLedgerAccount;
	/**
	 * 平台名称
	 */
	@ApiModelProperty(value = "平台名称")
	private String platformName;
	/**
	 * 平台分账比例
	 */
	@ApiModelProperty(value = "平台分账比例")
	private Double platformPercent;
	/**
	 * 平台分账金额
	 */
	@ApiModelProperty(value = "平台分账金额")
	private Double platformLegerAccount;
	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	/**
	 * 商家分账比例
	 */
	@ApiModelProperty(value = "商家分账比例")
	private Double merchantPercent;
	/**
	 * 商家分账金额
	 */
	@ApiModelProperty(value = "商家分账金额")
	private Double merchantLegerAccount;
	/**
	 * 消费订单号
	 */
	@ApiModelProperty(value = "消费订单号")
	private String orderNumber;
	/**
	 * 应付金额
	 */
	@ApiModelProperty(value = "应付金额")
	private Double payable;
	/**
	 * 折扣金额
	 */
	@ApiModelProperty(value = "折扣金额")
	private Double discount;
	/**
	 * 实付金额（用户实际付款金额）
	 */
	@ApiModelProperty(value = "实付金额（用户实际付款金额）")
	private Double money;
	/**
	 * 分账日期   yyyy-MM-dd
	 */
	@ApiModelProperty(value = "分账日期 yyyy-MM-dd")
	private LocalDate ledgerTime;

	/**
	 * 核对账单生成状态  WAIT 待生成  CREATED 已生成
	 */
	@ApiModelProperty(value = "分账订单审核状态")
	private String ledgerCreateStatus;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 创建人姓名
	 */
	@ApiModelProperty(value = "创建人姓名")
	private String createBy;


}
