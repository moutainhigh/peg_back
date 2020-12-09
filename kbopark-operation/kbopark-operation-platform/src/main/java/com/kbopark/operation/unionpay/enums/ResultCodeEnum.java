package com.kbopark.operation.unionpay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:sunwenzhi
 * @Description:	银商分账返回码
 * @Date: 2020/9/23 8:36
 **/
@AllArgsConstructor
public enum ResultCodeEnum {

	R99999999("99999999","明细处理成功!"),
	FAN00001("FAN00001","商户不存在或者暂无多应用信息！"),
	FAN00002("FAN00002","订单不存在或者可划付金额为零！"),
	FAN00003("FAN00003","当前订单可划付金额不足！"),
	FAN00004("FAN00004","当前商户可划付金额不足！"),
	FAN00005("FAN00005","订单所在多应用可划付金额不足！"),
	FAN00006("FAN00006","数据错误！"),
	FAN00007("FAN00007","参数不足！"),
	FAN00008("FAN00008","订单划付失败！"),
	FAN00009("FAN00009","未知划付类型！"),
	FAN00010("FAN00010","分账方不存在！"),
	FAN00011("FAN00011","查询出多条记录，不符合要求！"),
	FAN00012("FAN00012","暂未查询到该记录！"),
	FAN00013("FAN00013","商户不属于该集团！"),
	FAN00014("FAN00014","打解包失败！"),
	FAN00015("FAN00015","退货失败！"),
	FAN00016("FAN00016","商户无权限进行此类交易！"),
	FAN00017("FAN00017","请求失败，重复操作，请注意！"),
	FAN00018("FAN00018","可退货余额不足！"),
	FAN00019("FAN00019","商户有进行中交易，请稍后重试！"),
	FAN00020("FAN00020","商户交易金额应大于手续费金额"),
	R11111111("11111111","未知错误！"),
	;
	@Getter
	private String code;

	@Getter
	private String msg;

}
