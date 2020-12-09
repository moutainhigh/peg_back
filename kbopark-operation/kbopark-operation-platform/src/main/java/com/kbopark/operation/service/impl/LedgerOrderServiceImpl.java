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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.RandomUtil;
import cn.qdzhhl.kbopark.admin.api.enums.UserTypeEnum;
import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.service.KboparkUser;
import cn.qdzhhl.kbopark.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.dto.LedgerAuditDTO;
import com.kbopark.operation.entity.*;
import com.kbopark.operation.enums.*;
import com.kbopark.operation.mapper.LedgerOrderMapper;
import com.kbopark.operation.mapper.MerchantMapper;
import com.kbopark.operation.service.*;
import com.kbopark.operation.unionpay.dto.MoneySubAccountDTO;
import com.kbopark.operation.unionpay.dto.SuperDto;
import com.kbopark.operation.unionpay.enums.ResultCodeEnum;
import com.kbopark.operation.unionpay.enums.TransCodeEnum;
import com.kbopark.operation.unionpay.exceptions.SecureUtilException;
import com.kbopark.operation.unionpay.service.SubAccountService;
import com.kbopark.operation.unionpay.util.SuperUtil;
import com.kbopark.operation.util.OperationConstants;
import com.kbopark.operation.util.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分账订单记录
 *
 * @author pigx code generator
 * @date 2020-09-14 13:50:42
 */
@Service
@Slf4j
@AllArgsConstructor
public class LedgerOrderServiceImpl extends ServiceImpl<LedgerOrderMapper, LedgerOrder> implements LedgerOrderService {

	private final MerchantMapper merchantMapper;

	private final MerchantBalanceService merchantBalanceService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R createLedgerOrder(ConsumerOrder byOrderNumber) {
		log.info(">>>创建分账订单："+ JSONObject.toJSONString(byOrderNumber));
		if(byOrderNumber == null){
			return R.failed(OperationConstants.LEDGER_ERROR);
		}
		Integer merchantId = byOrderNumber.getMerchantId();
		Merchant merchant = merchantMapper.selectById(merchantId);
		if(merchant == null){
			return R.failed(OperationConstants.MERCHANT_EMPTY);
		}
		//商家分润比例
		Double merchantPercent = merchant.getProfitPercent() == null ? OrderUtil.defaultMaxNum : merchant.getProfitPercent();
		//运营商分润比例
		Double operatorPercent = merchant.getOperatorProfitPercent() == null ? OrderUtil.defaultMinNum : merchant.getOperatorProfitPercent();
		//平台分润比例
		Double platFormPercent = OrderUtil.defaultMaxNum - merchantPercent - operatorPercent;

		//根据订单号查询是否已存在分账订单
		LedgerOrder oldLedger = findByOrderNum(byOrderNumber.getOrderNumber());
		LedgerOrder ledgerOrder = BeanUtil.copyProperties(byOrderNumber, LedgerOrder.class);
		ledgerOrder.setId(oldLedger == null ? null : oldLedger.getId());
		ledgerOrder.setLedgerSerialNumber(RandomUtil.randomString(32));
		ledgerOrder.setLedgerStatus(LedgerStatusEnum.UNSETTLE.getCode());

		//设置比例和金额
		double merchantMoney = OrderUtil.div(OrderUtil.mul(byOrderNumber.getMoney(), merchantPercent), OrderUtil.defaultMaxNum);
		double operationMoney = OrderUtil.div(OrderUtil.mul(byOrderNumber.getMoney(), operatorPercent), OrderUtil.defaultMaxNum);
		double m = byOrderNumber.getMoney() - merchantMoney - operationMoney;
		double platFormMoney = m < 0 ? 0 : m;
		ledgerOrder.setMerchantPercent(merchantPercent);
		ledgerOrder.setMerchantLegerAccount(merchantMoney);
		ledgerOrder.setOperationPercent(operatorPercent);
		ledgerOrder.setOperationLedgerAccount(operationMoney);
		ledgerOrder.setPlatformId(1);
		ledgerOrder.setPlatformName("总平台");
		ledgerOrder.setPlatformPercent(platFormPercent);
		ledgerOrder.setPlatformLegerAccount(platFormMoney);

		//其他信息
		ledgerOrder.setCreateTime(LocalDateTime.now());
		ledgerOrder.setUpdateTime(null);
		//核对账单生成状态
		ledgerOrder.setLedgerCreateStatus(LedgerCreateStatusEnum.WAIT.getCode());

		//更新
		saveOrUpdate(ledgerOrder);

		//分账订单生成后增加商家待入账金额
		merchantBalanceService.addToAccount(merchantId, merchantMoney);

		return R.ok(ledgerOrder);
	}


	@Override
	public LedgerOrder findByOrderNum(String orderNum) {
		QueryWrapper<LedgerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerOrder::getOrderNumber, orderNum);
		return getOne(queryWrapper, false);
	}


	@Override
	public List<LedgerOrder> findByLessEqualTime(Integer merchantId, Date beginDate) {
		QueryWrapper<LedgerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(LedgerOrder::getMerchantId, merchantId)
				.le(LedgerOrder::getCreateTime, beginDate)
				.eq(LedgerOrder::getLedgerStatus, LedgerStatusEnum.UNSETTLE.getCode());
		return list(queryWrapper);
	}


	@Override
	public List<LedgerOrder> findByLedgerBatchNo(String batchNo) {
		QueryWrapper<LedgerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(LedgerOrder::getLedgerBatchNo, batchNo);
		return list(queryWrapper);
	}


}
