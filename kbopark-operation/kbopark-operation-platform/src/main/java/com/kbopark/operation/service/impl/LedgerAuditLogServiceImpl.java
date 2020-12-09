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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.entity.LedgerAuditLog;
import com.kbopark.operation.mapper.LedgerAuditLogMapper;
import com.kbopark.operation.service.LedgerAuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分账订单审核日志
 *
 * @author pigx code generator
 * @date 2020-10-16 13:56:49
 */
@Service
public class LedgerAuditLogServiceImpl extends ServiceImpl<LedgerAuditLogMapper, LedgerAuditLog> implements LedgerAuditLogService {


	@Override
	public List<LedgerAuditLog> findByLedgerBatchNo(String batchNo) {
		QueryWrapper<LedgerAuditLog> logQueryWrapper = new QueryWrapper<>();
		logQueryWrapper.lambda()
				.eq(LedgerAuditLog::getLedgerBatchNo, batchNo)
				.orderByAsc(LedgerAuditLog::getAuditTime);
		return list(logQueryWrapper);
	}
}
