/*
 *    Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: kbopark
 */
package com.kbopark.operation.service.impl;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.entity.MerchantWelfareSetting;
import com.kbopark.operation.entity.SubwayTicket;
import com.kbopark.operation.enums.LockStatusEnum;
import com.kbopark.operation.mapper.MerchantMapper;
import com.kbopark.operation.mapper.MerchantWelfareSettingMapper;
import com.kbopark.operation.mapper.SubwayTicketMapper;
import com.kbopark.operation.service.MerchantWelfareSettingService;
import com.kbopark.operation.util.OperationConstants;
import com.kbopark.operation.util.OrderUtil;
import com.kbopark.operation.util.TicketRule;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家福利设置
 *
 * @author pigx code generator
 * @date 2020-09-09 15:53:58
 */
@Service
@AllArgsConstructor
public class MerchantWelfareSettingServiceImpl extends ServiceImpl<MerchantWelfareSettingMapper, MerchantWelfareSetting> implements MerchantWelfareSettingService {

	private final MerchantMapper merchantMapper;

	private final SubwayTicketMapper subwayTicketMapper;


	@Override
	public R saveAndCheckSetting(MerchantWelfareSetting merchantWelfareSetting) {
		String ruleInfo = merchantWelfareSetting.getRuleInfo();
		Double fullMoney = merchantWelfareSetting.getFullMoney();
		boolean b = checkSettingPercent(merchantWelfareSetting.getMerchantId(), fullMoney, ruleInfo);
		if(!b){
			return R.failed(OperationConstants.MERCHANT_SETTING_FULL);
		}

		return R.ok(save(merchantWelfareSetting));
	}

	@Override
	public R updateAndCheckSetting(MerchantWelfareSetting merchantWelfareSetting) {

		String ruleInfo = merchantWelfareSetting.getRuleInfo();
		Double fullMoney = merchantWelfareSetting.getFullMoney();
		boolean b = checkSettingPercent(merchantWelfareSetting.getMerchantId(), fullMoney, ruleInfo);
		if(!b){
			return R.failed(OperationConstants.MERCHANT_SETTING_FULL);
		}
		return R.ok(updateById(merchantWelfareSetting));
	}

	@Override
	public boolean checkSettingPercent(Integer merchantId, Double fullMoney, String ruleInfo) {
		Merchant merchant = merchantMapper.selectById(merchantId);
		merchant = merchant == null ? new Merchant() : merchant;
		//商家福利比例
		Double welfarePercent = merchant.getWelfarePercent();
		double couponMoney = 0;
		if (StringUtils.isNotBlank(ruleInfo)) {
			List<TicketRule> ticketRules = OrderUtil.collectFilter(ruleInfo);
			for (int i = 0; i < ticketRules.size(); i++) {
				TicketRule ticketRule = ticketRules.get(i);
				Integer couponNum = ticketRule.getCouponNum();
				couponNum = couponNum == null ? 0 : couponNum;
				SubwayTicket subwayTicket = subwayTicketMapper.selectById(ticketRule.getCouponId());
				//价值
				Double value = subwayTicket.getValue();
				value = value == null ? 0 : value;
				double mul = OrderUtil.mul(couponNum, value);
				couponMoney = OrderUtil.add(couponMoney, mul);
			}
			double div = OrderUtil.div(couponMoney, fullMoney, 2);
			double percent = OrderUtil.mul(div, 100);
			if(percent > welfarePercent){
				return false;
			}
		}
		return true;
	}
}
