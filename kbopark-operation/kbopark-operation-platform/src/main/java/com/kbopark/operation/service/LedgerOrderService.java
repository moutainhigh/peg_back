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
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.entity.LedgerOrder;
import com.kbopark.operation.unionpay.dto.MoneySubAccountDTO;

import java.util.Date;
import java.util.List;

/**
 * 分账订单记录
 *
 * @author pigx code generator
 * @date 2020-09-14 13:50:42
 */
public interface LedgerOrderService extends IService<LedgerOrder> {

	/**
	 * 根据消费订单创建待分账订单
	 *
	 * @param byOrderNumber
	 * @return
	 */
	R createLedgerOrder(ConsumerOrder byOrderNumber);


	/**
	 * 根据消费订单号查询分账订单
	 *
	 * @param orderNum
	 * @return
	 */
	LedgerOrder findByOrderNum(String orderNum);


	/**
	 * 根据商家ID和分账日期查询未结算得分账订单
	 *
	 * @param merchantId 商家ID
	 * @param beginDate  结算日
	 * @return 该商户结算日之前所有未结算得分账订单
	 */
	List<LedgerOrder> findByLessEqualTime(Integer merchantId, Date beginDate);


	/**
	 * 根据批次号查询分账订单
	 * @param batchNo
	 * @return
	 */
	List<LedgerOrder> findByLedgerBatchNo(String batchNo);



}
