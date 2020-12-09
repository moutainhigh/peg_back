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

package com.kbopark.operation.service;

import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.dto.LedgerAuditDTO;
import com.kbopark.operation.entity.LedgerAccount;
import com.kbopark.operation.entity.LedgerCheck;
import com.kbopark.operation.entity.LedgerOrder;
import com.kbopark.operation.unionpay.dto.MoneyPaymentDTO;
import com.kbopark.operation.unionpay.dto.MoneySubAccountDTO;
import com.kbopark.operation.unionpay.dto.SuperDto;

import java.util.List;

/**
 * 分账核对账单
 *
 * @author pigx code generator
 * @date 2020-10-19 11:49:30
 */
public interface LedgerCheckService extends IService<LedgerCheck> {


	/***
	 * 根据批次号查询核对订单
	 * @param batchNo
	 * @return
	 */
	LedgerCheck findByBatchNo(String batchNo);

	/**
	 * 处理统计分账订单并生成核对账单
	 *
	 * @param ledgerOrders
	 * @return
	 */
	LedgerCheck handleOrders(List<LedgerOrder> ledgerOrders);

	/**
	 * 根据商家账户和日期生成核对账单
	 *
	 * @param merchantId 商家ID
	 * @param dateRange  结算日期范围
	 * @return
	 */
	R createCheckByMerchant(Integer merchantId, List<String> dateRange);

	/***
	 * 根据分账订单选中值生成核对账单
	 *
	 * @param orderIds
	 * @return
	 */
	R createCheckByConsumerOrder(List<Long> orderIds);

	/**
	 * 财务审核更新
	 *
	 * @param ledgerAuditDTO
	 * @return
	 */
	R updateByAudit(LedgerAuditDTO ledgerAuditDTO);


	/***
	 * 通过分账核对订单处理分账数据
	 *
	 * @param ledgerCheck
	 * @return
	 */
	R handleSubAccountByLedgerCheck(LedgerCheck ledgerCheck);

	/**
	 * 执行按金额分账命令，在校验完核对订单状态后执行，同时生成分账明细
	 *
	 * @param moneySubAccountDTO 请求参数
	 * @param batchNo            分账批次号
	 * @param source             执行账户信息
	 * @param target             目标账户信息
	 * @return
	 */
	SuperDto sendSubAccountCommand(MoneySubAccountDTO moneySubAccountDTO, String batchNo, LedgerAccount source, LedgerAccount target);


	/***
	 * 执行按金额划付命令，在校验完核对订单状态后执行，同时生成分账明细
	 * @param moneyPaymentDTO    请求参数
	 * @param batchNo            批次号
	 * @param source            执行账户信息
	 * @return
	 */
	SuperDto sendMoneyPaymentCommand(MoneyPaymentDTO moneyPaymentDTO, String batchNo, LedgerAccount source);

}
