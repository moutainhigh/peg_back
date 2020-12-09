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
import cn.hutool.core.date.DateTime;
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
import com.kbopark.operation.mapper.LedgerCheckMapper;
import com.kbopark.operation.mapper.LedgerOrderMapper;
import com.kbopark.operation.service.*;
import com.kbopark.operation.unionpay.dto.MoneyPaymentDTO;
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
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分账核对账单
 *
 * @author pigx code generator
 * @date 2020-10-19 11:49:30
 */
@Service
@Slf4j
@AllArgsConstructor
public class LedgerCheckServiceImpl extends ServiceImpl<LedgerCheckMapper, LedgerCheck> implements LedgerCheckService {

	private final LedgerOrderService ledgerOrderService;

	private final LedgerAuditStrategyService ledgerAuditStrategyService;

	private final LedgerAuditLogService ledgerAuditLogService;

	private final SubAccountService subAccountService;

	private final LedgerDetailService ledgerDetailService;

	private final LedgerAccountService ledgerAccountService;

	private final MerchantBalanceService merchantBalanceService;


	@Override
	public LedgerCheck findByBatchNo(String batchNo) {
		QueryWrapper<LedgerCheck> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerCheck::getLedgerBatchNo ,batchNo);
		return getOne(queryWrapper, false);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public LedgerCheck handleOrders(List<LedgerOrder> ledgerOrders) {
		if(CollectionUtils.isNotEmpty(ledgerOrders)){
			LedgerOrder ledgerOrderDetail = ledgerOrders.get(0);
			LedgerCheck ledgerCheck = BeanUtil.copyProperties(ledgerOrderDetail, LedgerCheck.class);
			ledgerCheck.setId(null);
			ledgerCheck.setCreateTime(LocalDateTime.now());
			ledgerCheck.setLedgerSerialNumber(RandomUtil.randomString(32));
			ledgerCheck.setLedgerBatchNo(OrderUtil.getSerialNumber("TDN"));
			ledgerCheck.setLedgerStatus(LedgerStatusEnum.UNSETTLE.getCode());
			//求分账总金额
			DoubleSummaryStatistics merchantMoney = ledgerOrders.stream().collect(Collectors.summarizingDouble(LedgerOrder::getMerchantLegerAccount));
			DoubleSummaryStatistics operationMoney = ledgerOrders.stream().collect(Collectors.summarizingDouble(LedgerOrder::getOperationLedgerAccount));
			DoubleSummaryStatistics platFormMoney = ledgerOrders.stream().collect(Collectors.summarizingDouble(LedgerOrder::getPlatformLegerAccount));
			ledgerCheck.setMerchantLegerAccount(merchantMoney.getSum());
			ledgerCheck.setOperationLedgerAccount(operationMoney.getSum());
			ledgerCheck.setPlatformLegerAccount(platFormMoney.getSum());

			//设置人员信息
			KboparkUser user = SecurityUtils.getUser();
			ledgerCheck.setCreateBy(user.getRealName());

			//设置默认审核状态并更新审核日志
			setLedgerAuditStatus(ledgerCheck);
			//新增核对账单
			save(ledgerCheck);
			//增加判断，账单状态审核通过直接分账
			if(LedgerAuditStatusEnum.SUCCESS.getCode().equals(ledgerCheck.getLedgerAuditStatus())){
				//处理创建账单时如果运营和平台都设置了自动通过审核则自动执行分账
				handleSubAccountByLedgerCheck(ledgerCheck);
			}

			//更新分账订单状态
			for (LedgerOrder ledgerOrder : ledgerOrders) {
				ledgerOrder.setLedgerCreateStatus(LedgerCreateStatusEnum.CREATED.getCode());
				ledgerOrder.setLedgerBatchNo(ledgerCheck.getLedgerBatchNo());
			}
			ledgerOrderService.updateBatchById(ledgerOrders);

			return ledgerCheck;
		}else{
			return null;
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R createCheckByMerchant(Integer merchantId, List<String> dateRange) {
		if(merchantId == null){
			return R.failed("请选择商家");
		}
		if(CollectionUtils.isEmpty(dateRange)){
			return R.failed("请选择结算日期范围");
		}
		DateTime rangStart = DateUtil.parseDate(dateRange.get(0));
		DateTime rangeEnd = DateUtil.parseDate(dateRange.get(1));
		DateTime beginOfDay = DateUtil.beginOfDay(rangStart);
		DateTime endOfDay = DateUtil.endOfDay(rangeEnd);
		//查询未生成核对账单得分账订单
		QueryWrapper<LedgerOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(LedgerOrder::getMerchantId, merchantId)
				.eq(LedgerOrder::getLedgerCreateStatus, LedgerCreateStatusEnum.WAIT.getCode())
				.between(LedgerOrder::getCreateTime, beginOfDay, endOfDay);
		List<LedgerOrder> orderList = ledgerOrderService.list(queryWrapper);
		if(CollectionUtils.isEmpty(orderList)){
			return R.failed("商家在此段时间内暂无分账订单");
		}
		LedgerCheck ledgerCheck = handleOrders(orderList);
		return R.ok(ledgerCheck);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R createCheckByConsumerOrder(List<Long> orderIds) {
		log.info(">>>根据分账订单创建核对账单参数："+JSONObject.toJSONString(orderIds));
		if(CollectionUtils.isEmpty(orderIds)){
			return R.failed("未选择分账订单");
		}
		List<LedgerOrder> ledgerOrders = ledgerOrderService.listByIds(orderIds);
		LedgerCheck ledgerCheck = handleOrders(ledgerOrders);
		return R.ok(ledgerCheck);
	}

	/***
	 * 设置审核状态
	 * @param ledgerCheck
	 */
	private void setLedgerAuditStatus(LedgerCheck ledgerCheck){
		//设置默认审核状态
		ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.WAIT.getCode());
		ledgerCheck.setOperationAuditStatus(OperationAuditStatusEnum.B_WAIT.getCode());
		ledgerCheck.setPlatformAuditStatus(PlatformAuditStatusEnum.B_WAIT.getCode());
		//根据审核策略设置审核状态
		LedgerAuditStrategy operationStrategy = ledgerAuditStrategyService.findByDeptId(ledgerCheck.getOperationId());
		LedgerAuditStrategy platFormStrategy = ledgerAuditStrategyService.findByDeptId(1);
		log.info(">>>分账订单状态设置，OP机构ID:"+ledgerCheck.getOperationId()+",当前审核策略："+JSONObject.toJSONString(operationStrategy));
		List<LedgerAuditLog> ledgerAuditLogList = new ArrayList<>();
		if(operationStrategy != null){
			if(LedgerStrategyEnum.AUTO.getCode().equals(operationStrategy.getBusinessType())){
				ledgerCheck.setOperationAuditStatus(OperationAuditStatusEnum.B_SUCCESS.getCode());
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.AUDITING.getCode());
				LedgerAuditLog ledgerAuditLog = new LedgerAuditLog();
				ledgerAuditLog.setAuditMsg(OperationAuditStatusEnum.B_SUCCESS.getDescription());
				ledgerAuditLog.setAuditStatus(OperationAuditStatusEnum.B_SUCCESS.toString());
				ledgerAuditLog.setDeptId(ledgerCheck.getOperationId());
				ledgerAuditLogList.add(ledgerAuditLog);
			}
			if(LedgerStrategyEnum.AUTO.getCode().equals(operationStrategy.getFinancialType())){
				ledgerCheck.setOperationAuditStatus(OperationAuditStatusEnum.F_SUCCESS.getCode());
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.AUDITING.getCode());
				LedgerAuditLog ledgerAuditLog = new LedgerAuditLog();
				ledgerAuditLog.setAuditMsg(OperationAuditStatusEnum.F_SUCCESS.getDescription());
				ledgerAuditLog.setAuditStatus(OperationAuditStatusEnum.F_SUCCESS.toString());
				ledgerAuditLog.setDeptId(ledgerCheck.getOperationId());
				ledgerAuditLogList.add(ledgerAuditLog);
			}
		}
		if(platFormStrategy != null){
			if(LedgerStrategyEnum.AUTO.getCode().equals(platFormStrategy.getBusinessType())){
				ledgerCheck.setPlatformAuditStatus(PlatformAuditStatusEnum.B_SUCCESS.getCode());
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.AUDITING.getCode());
				LedgerAuditLog ledgerAuditLog = new LedgerAuditLog();
				ledgerAuditLog.setAuditMsg(PlatformAuditStatusEnum.B_SUCCESS.getDescription());
				ledgerAuditLog.setAuditStatus(PlatformAuditStatusEnum.B_SUCCESS.toString());
				ledgerAuditLog.setDeptId(1);
				ledgerAuditLogList.add(ledgerAuditLog);
			}
			if(LedgerStrategyEnum.AUTO.getCode().equals(platFormStrategy.getFinancialType())){
				//平台财务自动审核通过会直接分账
				ledgerCheck.setPlatformAuditStatus(PlatformAuditStatusEnum.F_SUCCESS.getCode());
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.SUCCESS.getCode());
				LedgerAuditLog ledgerAuditLog = new LedgerAuditLog();
				ledgerAuditLog.setAuditMsg(PlatformAuditStatusEnum.F_SUCCESS.getDescription());
				ledgerAuditLog.setAuditStatus(PlatformAuditStatusEnum.F_SUCCESS.toString());
				ledgerAuditLog.setDeptId(1);
				ledgerAuditLogList.add(ledgerAuditLog);
			}
		}
		//更新保存审核日志
		if(CollectionUtils.isNotEmpty(ledgerAuditLogList)){
			for (LedgerAuditLog ledgerAuditLog : ledgerAuditLogList) {
				ledgerAuditLog.setAuditBy(LedgerStrategyEnum.AUTO.getName());
				ledgerAuditLog.setAuditRemark(LedgerStrategyEnum.AUTO.getName());
				ledgerAuditLog.setAuditTime(LocalDateTime.now());
				ledgerAuditLog.setCreateBy(LedgerStrategyEnum.AUTO.getName());
				ledgerAuditLog.setLedgerBatchNo(ledgerCheck.getLedgerBatchNo());
			}
			ledgerAuditLogService.saveBatch(ledgerAuditLogList);
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R updateByAudit(LedgerAuditDTO ledgerAuditDTO) {
		//TODO
		log.info(">>>分账审核更新接收参数："+ JSONObject.toJSONString(ledgerAuditDTO));
		KboparkUser user = SecurityUtils.getUser();
		log.info(">>>当前用户类型："+user.getUserType());
		log.info(">>>当前用户所属机构ID："+user.getDeptId());
		//1.更新订单状态
		//2.增加审核日志
		LedgerCheck ledgerCheck = findByBatchNo(ledgerAuditDTO.getLedgerBatchNo());
		if(ledgerCheck == null){
			return R.failed(OperationConstants.LEDGER_AUDIT_NO_ORDER);
		}
		//审核状态验证
		if(LedgerAuditStatusEnum.FAILED.getCode().equals(ledgerCheck.getLedgerAuditStatus())
				|| LedgerAuditStatusEnum.SUCCESS.getCode().equals(ledgerCheck.getLedgerAuditStatus())){
			return R.failed(OperationConstants.LEDGER_AUDIT_FINISHED);
		}

		//当前审核人员是运营
		if(UserTypeEnum.Operation.getCode().equals(user.getUserType())){
			Integer currentStatus = ledgerCheck.getOperationAuditStatus();
			Integer nextStatus = EnumUtil.fromString(OperationAuditStatusEnum.class, ledgerAuditDTO.getAuditStatus()).getCode();
			if(OperationAuditStatusEnum.B_WAIT.getCode().equals(currentStatus) && nextStatus >= OperationAuditStatusEnum.F_FAIL.getCode()){
				return R.failed(OperationConstants.LEDGER_AUDIT_WAIT_B);
			}
		}else{
			//当前审核人员是平台
			Integer currentOperationStatus = ledgerCheck.getOperationAuditStatus();
			Integer currentStatus = ledgerCheck.getPlatformAuditStatus();
			Integer nextStatus = EnumUtil.fromString(PlatformAuditStatusEnum.class, ledgerAuditDTO.getAuditStatus()).getCode();
			if(OperationAuditStatusEnum.B_WAIT.getCode().equals(currentOperationStatus) || OperationAuditStatusEnum.B_SUCCESS.getCode().equals(currentOperationStatus)){
				return R.failed(OperationConstants.LEDGER_AUDIT_WAIT_OPE);
			}
			if(PlatformAuditStatusEnum.B_WAIT.getCode().equals(currentStatus) && nextStatus >= PlatformAuditStatusEnum.F_FAIL.getCode()){
				return R.failed(OperationConstants.LEDGER_AUDIT_WAIT_B);
			}
		}

		//提取审核状态
		ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.AUDITING.getCode());
		String auditStatus = ledgerAuditDTO.getAuditStatus();
		Integer code = EnumUtil.fromString(OperationAuditStatusEnum.class, auditStatus).getCode();
		//运营审核
		if(UserTypeEnum.Operation.getCode().equals(user.getUserType())){
			ledgerCheck.setOperationAuditStatus(code);
			if(OperationAuditStatusEnum.B_FAIL.toString().equals(auditStatus)
					|| OperationAuditStatusEnum.F_FAIL.toString().equals(auditStatus)){
				ledgerCheck.setLedgerStatus(LedgerStatusEnum.NOSETTLE.getCode());
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.FAILED.getCode());
			}
		}
		//平台审核
		if(UserTypeEnum.Administrator.getCode().equals(user.getUserType())){
			ledgerCheck.setPlatformAuditStatus(code);
			if(PlatformAuditStatusEnum.B_FAIL.toString().equals(auditStatus)
					|| PlatformAuditStatusEnum.F_FAIL.toString().equals(auditStatus)){
				ledgerCheck.setLedgerStatus(LedgerStatusEnum.NOSETTLE.getCode());
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.FAILED.getCode());
			}
			if(PlatformAuditStatusEnum.F_SUCCESS.toString().equals(auditStatus)){
				ledgerCheck.setLedgerAuditStatus(LedgerAuditStatusEnum.SUCCESS.getCode());
			}
		}
		ledgerCheck.setUpdateTime(LocalDateTime.now());
		//更新对账单状态
		boolean b = updateById(ledgerCheck);
		//增加判断，账单状态审核通过直接分账
		if(LedgerAuditStatusEnum.SUCCESS.getCode().equals(ledgerCheck.getLedgerAuditStatus()) && b){
			//人工审核，最终核对账单通过审核时自动分账
			handleSubAccountByLedgerCheck(ledgerCheck);
		}

		//设置审核日志
		ledgerAuditDTO.setAuditBy(user.getRealName());
		if(UserTypeEnum.Operation.getCode().equals(user.getUserType())){
			String description = EnumUtil.fromString(OperationAuditStatusEnum.class, auditStatus).getDescription();
			ledgerAuditDTO.setAuditMsg(description);
		}else{
			String description = EnumUtil.fromString(PlatformAuditStatusEnum.class, auditStatus).getDescription();
			ledgerAuditDTO.setAuditMsg(description);
		}
		ledgerAuditDTO.setAuditTime(LocalDateTime.now());
		ledgerAuditDTO.setCreateTime(LocalDateTime.now());
		ledgerAuditDTO.setCreateBy(user.getRealName());
		ledgerAuditDTO.setDeptId(user.getDeptId());
		ledgerAuditDTO.setLedgerBatchNo(ledgerCheck.getLedgerBatchNo());
		ledgerAuditLogService.save(ledgerAuditDTO);
		return R.ok(true);
	}


	/**
	 * 1.根据配置的分账账户执行分账
	 * 2.根据指定的商家账户搜索分账订单，结算金额
	 * 3.分账成功后要更新分账订单状态和批次号
	 * @param ledgerCheck
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R handleSubAccountByLedgerCheck(LedgerCheck ledgerCheck) {
		//TODO
		//账单不存在不执行分账
		if(ledgerCheck == null){
			return R.failed(OperationConstants.LEDGER_CHECK_NO_ORDER);
		}
		//账单不是待分账状态不执行分账
		if(!LedgerStatusEnum.UNSETTLE.getCode().equals(ledgerCheck.getLedgerStatus())){
			return R.failed(OperationConstants.LEDGER_CHECK_NOT_WAIT);
		}
		//账单无明细不执行分账
		List<LedgerOrder> ledgerOrderList = ledgerOrderService.findByLedgerBatchNo(ledgerCheck.getLedgerBatchNo());
		if(CollectionUtils.isEmpty(ledgerOrderList)){
			return R.failed(OperationConstants.LEDGER_CHECK_NO_DETAIL);
		}
		//商家主体账户未配置不执行分账
		LedgerAccount merchantAccount = ledgerAccountService.findByDeptId(ledgerCheck.getMerchantId());
		if(merchantAccount == null){
			return R.failed(OperationConstants.LEDGER_NO_MERCHANT_ACCOUNT);
		}
		//运营商账户未配置不执行分账
		LedgerAccount operationAccount = ledgerAccountService.findByDeptId(ledgerCheck.getOperationId());
		if(operationAccount == null){
			return R.failed(OperationConstants.LEDGER_NO_OPERATION_ACCOUNT);
		}
		//平台账户未配置不执行分账
		LedgerAccount platformAccount = ledgerAccountService.findByDeptId(ledgerCheck.getPlatformId());
		if(platformAccount == null){
			return R.failed(OperationConstants.LEDGER_NO_PLATFORM_ACCOUNT);
		}
		Double merchantSubMoney = ledgerCheck.getMerchantLegerAccount();
		Double operationSubMoney = ledgerCheck.getOperationLedgerAccount();
		Double platformSubMoney = ledgerCheck.getPlatformLegerAccount();
		Double mm = OrderUtil.mul(merchantSubMoney, 100);
		Double om = OrderUtil.mul(operationSubMoney, 100);
		Double pm = OrderUtil.mul(platformSubMoney, 100);
		MoneySubAccountDTO subOne = new MoneySubAccountDTO(
				merchantAccount.getMerNo(),
				operationAccount.getCardNo(),
				OperationConstants.SYSTEM_SUB_DEFAULT_PS, String.valueOf(om.intValue()));
		MoneySubAccountDTO subTwo = new MoneySubAccountDTO(
				merchantAccount.getMerNo(),
				platformAccount.getCardNo(),
				OperationConstants.SYSTEM_SUB_DEFAULT_PS, String.valueOf(pm.intValue()));
		MoneyPaymentDTO payment = new MoneyPaymentDTO(
				merchantAccount.getMerNo(),
				String.valueOf(mm.intValue()));
		SuperDto resultOne = sendSubAccountCommand(subOne, ledgerCheck.getLedgerBatchNo(), merchantAccount, operationAccount);
		SuperDto resultTwo = sendSubAccountCommand(subTwo, ledgerCheck.getLedgerBatchNo(), merchantAccount, platformAccount);
		SuperDto resultThree = sendMoneyPaymentCommand(payment, ledgerCheck.getLedgerBatchNo(), merchantAccount);
		//分账命令执行成功则更新
		if(ResultCodeEnum.R99999999.getCode().equals(resultThree.getRespCode())){
			ledgerCheck.setLedgerStatus(LedgerStatusEnum.SETTLED.getCode());
			//商家分账执行成功后调用账户资金管理服务
			merchantBalanceService.subAccount(ledgerCheck.getMerchantId(), merchantSubMoney);
		}else{
			ledgerCheck.setLedgerStatus(LedgerStatusEnum.FAILED.getCode());
		}
		//更新核对账单
		ledgerCheck.setLedgerTime(LocalDate.now());
		this.updateById(ledgerCheck);

		if(CollectionUtils.isNotEmpty(ledgerOrderList)){
			for (LedgerOrder ledgerOrder : ledgerOrderList) {
				ledgerOrder.setLedgerBatchNo(ledgerCheck.getLedgerBatchNo());
				ledgerOrder.setLedgerTime(LocalDate.now());
				if(ResultCodeEnum.R99999999.getCode().equals(resultThree.getRespCode())){
					ledgerOrder.setLedgerStatus(LedgerStatusEnum.SETTLED.getCode());
				}else{
					ledgerOrder.setLedgerStatus(LedgerStatusEnum.FAILED.getCode());
				}
			}
			//批量更新操作
			ledgerOrderService.updateBatchById(ledgerOrderList);
		}

		return R.ok(ledgerCheck);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SuperDto sendSubAccountCommand(MoneySubAccountDTO moneySubAccountDTO, String batchNo, LedgerAccount source, LedgerAccount target) {
		//执行分账命令
		R result = subAccountService.executeMoneySubAccount(moneySubAccountDTO);
		SuperDto data = (SuperDto) result.getData();
		//结果处理，生成明细
		LedgerDetail detail = new LedgerDetail();
		detail.setLedgerBatchNo(batchNo);
		detail.setTransCode(TransCodeEnum.T202004.getCode());
		detail.setTransName(TransCodeEnum.T202004.getName());
		detail.setTransDate(data.getSrcReqDate());
		detail.setTransTime(data.getSrcReqTime());
		detail.setSrcReqId(data.getSrcReqId());

		detail.setRespCode(data.getRespCode());
		detail.setRespMsg(data.getRespMsg());
		if(ResultCodeEnum.R99999999.getCode().equals(data.getRespCode())){
			detail.setTransStatus(OrderStatusEnum.SUCCESS.getCode());
		}else{
			detail.setTransStatus(OrderStatusEnum.FAIL.getCode());
		}
		KboparkUser user = SecurityUtils.getUser();
		detail.setCreateBy(user.getRealName());
		detail.setMerchantId(source.getRelationId());
		detail.setMerchantMerNo(source.getMerNo());
		detail.setMerchantName(source.getMerName());
		detail.setSubAccountName(target.getMerName());
		if(BelongTypeEnum.OPERATION.getCode().equals(target.getBelongType())){
			detail.setOperationId(target.getRelationId());
		}
		try {
			detail.setSubCardNo(SuperUtil.hashHex(moneySubAccountDTO.getCardNo(),"SHA-256"));
		} catch (SecureUtilException e) {
			e.printStackTrace();
		}
		detail.setPayPs(moneySubAccountDTO.getPs());
		detail.setPayAmt(OrderUtil.div(Double.valueOf(moneySubAccountDTO.getPayAmt()), 100));
		ledgerDetailService.save(detail);
		//返回执行结果
		return data;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public SuperDto sendMoneyPaymentCommand(MoneyPaymentDTO moneyPaymentDTO, String batchNo, LedgerAccount source) {

		R result = subAccountService.executeMoneyPayment(moneyPaymentDTO);
		SuperDto data = (SuperDto) result.getData();
		//结果处理，生成明细
		LedgerDetail detail = new LedgerDetail();
		detail.setLedgerBatchNo(batchNo);
		detail.setTransCode(TransCodeEnum.T202002.getCode());
		detail.setTransName(TransCodeEnum.T202002.getName());
		detail.setTransDate(data.getSrcReqDate());
		detail.setTransTime(data.getSrcReqTime());
		detail.setSrcReqId(data.getSrcReqId());

		detail.setRespCode(data.getRespCode());
		detail.setRespMsg(data.getRespMsg());
		if(ResultCodeEnum.R99999999.getCode().equals(data.getRespCode())){
			detail.setTransStatus(OrderStatusEnum.SUCCESS.getCode());
		}else{
			detail.setTransStatus(OrderStatusEnum.FAIL.getCode());
		}
		KboparkUser user = SecurityUtils.getUser();
		detail.setCreateBy(user.getRealName());
		detail.setMerchantId(source.getRelationId());
		detail.setMerchantMerNo(source.getMerNo());
		detail.setMerchantName(source.getMerName());
		detail.setPayAmt(OrderUtil.div(Double.valueOf(moneyPaymentDTO.getPayAmt()), 100));
		ledgerDetailService.save(detail);
		//返回执行结果
		return data;
	}
}
