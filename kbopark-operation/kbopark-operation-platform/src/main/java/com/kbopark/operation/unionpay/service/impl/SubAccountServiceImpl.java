package com.kbopark.operation.unionpay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qdzhhl.kbopark.common.core.util.R;
import com.kbopark.operation.entity.LedgerRequestLog;
import com.kbopark.operation.service.LedgerRequestLogService;
import com.kbopark.operation.unionpay.config.AccountConfig;
import com.kbopark.operation.unionpay.dto.*;
import com.kbopark.operation.unionpay.enums.TransCodeEnum;
import com.kbopark.operation.unionpay.service.SubAccountService;
import com.kbopark.operation.unionpay.util.ExecuteLogger;
import com.kbopark.operation.unionpay.util.SuperUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/23 8:35
 **/
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SubAccountServiceImpl implements SubAccountService {

	private final AccountConfig accountConfig;

	private final LedgerRequestLogService ledgerRequestLogService;


	private void setDefaultConfig(SuperDto superDto, String transCode){
		superDto.setTransCode(transCode);
		if(StringUtils.isBlank(superDto.getMerNo())){
			superDto.setMerNo(accountConfig.getMerNo());
		}
	}

	@Override
	public R executeOrderPayment(OrderPaymentDTO orderPaymentDTO) {
		SuperDto superDto = BeanUtil.copyProperties(orderPaymentDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202001.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202001.getCode());
		executeLogger.setTransName(TransCodeEnum.T202001.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}

	@Override
	public R executeOrderSubAccount(OrderSubAccountDTO orderSubAccountDTO) {
		SuperDto superDto = BeanUtil.copyProperties(orderSubAccountDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202003.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202003.getCode());
		executeLogger.setTransName(TransCodeEnum.T202003.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}

	@Override
	public R executeMoneyPayment(MoneyPaymentDTO moneyPaymentDTO) {
		//按金额划付
		SuperDto superDto = BeanUtil.copyProperties(moneyPaymentDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202002.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202002.getCode());
		executeLogger.setTransName(TransCodeEnum.T202002.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}

	@Override
	public R executeMoneySubAccount(MoneySubAccountDTO moneySubAccountDTO) {
		//按金额分账
		SuperDto superDto = BeanUtil.copyProperties(moneySubAccountDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202004.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		//分账类型,0-指定金额分账（目前只支持0）
		superDto.setPayType("0");
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202004.getCode());
		executeLogger.setTransName(TransCodeEnum.T202004.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}

	@Override
	public R executeAccountInfo(AccountInfoDTO accountInfoDTO) {
		//查询银商账户信息
		SuperDto superDto = BeanUtil.copyProperties(accountInfoDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202006.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202006.getCode());
		executeLogger.setTransName(TransCodeEnum.T202006.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}

	@Override
	public R executeTradeDetail(TradeDetailDTO tradeDetailDTO) {
		SuperDto superDto = BeanUtil.copyProperties(tradeDetailDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202007.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202007.getCode());
		executeLogger.setTransName(TransCodeEnum.T202007.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}

	@Override
	public R executeHandleRecord(HandleRecordDTO handleRecordDTO) {
		SuperDto superDto = BeanUtil.copyProperties(handleRecordDTO, SuperDto.class);
		setDefaultConfig(superDto, TransCodeEnum.T202008.getCode());
		superDto.setSrcReqId(SuperUtil.getSerialNumber(null));
		ExecuteLogger executeLogger = SuperUtil.oneStep(superDto);
		executeLogger.setSrcReqId(superDto.getSrcReqId());
		executeLogger.setTransCode(TransCodeEnum.T202008.getCode());
		executeLogger.setTransName(TransCodeEnum.T202008.getName());
		LedgerRequestLog ledgerRequestLog = BeanUtil.copyProperties(executeLogger, LedgerRequestLog.class);
		ledgerRequestLogService.save(ledgerRequestLog);
		return R.ok(executeLogger.getSuperDto());
	}
}
