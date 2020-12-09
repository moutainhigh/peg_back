package com.kbopark.operation.util;

import org.springframework.stereotype.Component;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/3 14:52
 **/
@Component
public class OperationConstants {

	public static final String SYSTEM_SUB_DEFAULT_PS = "系统分账";
	public static final String ORDER_SUFFIX = "KBC";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String MERCHANT_EMPTY = "商家信息不存在";
	public static final String MERCHANT_SETTING_FULL = "发券金额不能超出商家福利比例";
	public static final String MERCHANT_NO_WELFARE_SETTING = "商家未设置福利";
	public static final String MONEY_NOT_FLL_SETTING = "消费未达到福利条件";
	public static final String ORDER_ERROR = "订单创建失败，请重新操作";
	public static final String NOTIFY_ORDER_NO_DATA = "订单号错误";
	public static final String ORDER_NO_PAY = "订单未支付";

	public static final String COUPON_UNAVAILABLE_EMPTY = "不可用，未查询到优惠信息";
	public static final String COUPON_UNAVAILABLE_START_TIME = "不可用，未到有效期";
	public static final String COUPON_UNAVAILABLE_END_TIME = "不可用，已过有效期";
	public static final String COUPON_UNAVAILABLE_BANNED = "不可用，已禁用";
	public static final String COUPON_UNAVAILABLE_DOWN = "不可用，已下架";
	public static final String COUPON_NO_RECEIVE = "暂无可用优惠信息";

	public static final String COUPON_NO_STOCK = "优惠券已抢完";
	public static final String COUPON_FULL_LIMIT = "您已领取优惠券，当前已达到最多领取限制，可到优惠券列表查看";
	public static final String RED_PACK_FULL = "红包已抢完";

	public static final String SUBWAY_LINE_NO_DATA = "暂无数据";
	public static final String SUBWAY_POST_ERROR = "发券失败，请求数据异常";

	public static final String LEDGER_ERROR = "无订单数据，创建分账订单失败";
	public static final String LEDGER_NO_MERCHANT_ACCOUNT = "商家分账账户未配置";
	public static final String LEDGER_NO_OPERATION_ACCOUNT = "运营商分账账户未配置";
	public static final String LEDGER_NO_PLATFORM_ACCOUNT = "平台分账账户未配置";
	public static final String LEDGER_NO_MERCHANT = "非商家账户或商家ID不存在";
	public static final String LEDGER_NO_ORDERS = "结算商户无可用分账订单";

	public static final String LEDGER_AUDIT_NO_ORDER = "未查询到分账订单";
	public static final String LEDGER_AUDIT_FINISHED = "订单已审核完结";
	public static final String LEDGER_AUDIT_WAIT_B = "业务人员未审核，请稍后处理";
	public static final String LEDGER_AUDIT_WAIT_OPE = "运营方尚未审核，请稍后处理";


	public static final String LEDGER_CHECK_NO_ORDER = "核对账单不存在";
	public static final String LEDGER_CHECK_NO_DETAIL = "未查询到核对账单明细";
	public static final String LEDGER_CHECK_NOT_WAIT = "非待分账状态不可执行";

}
