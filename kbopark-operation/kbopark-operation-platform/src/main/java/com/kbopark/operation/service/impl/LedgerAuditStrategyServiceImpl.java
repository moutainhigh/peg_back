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
import com.kbopark.operation.entity.LedgerAuditStrategy;
import com.kbopark.operation.mapper.LedgerAuditStrategyMapper;
import com.kbopark.operation.service.LedgerAuditStrategyService;
import org.springframework.stereotype.Service;

/**
 * 财务审核策略设置
 *
 * @author pigx code generator
 * @date 2020-10-15 16:15:31
 */
@Service
public class LedgerAuditStrategyServiceImpl extends ServiceImpl<LedgerAuditStrategyMapper, LedgerAuditStrategy> implements LedgerAuditStrategyService {


	@Override
	public LedgerAuditStrategy findByDeptId(Integer deptId) {
		QueryWrapper<LedgerAuditStrategy> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAuditStrategy::getDeptId, deptId);
		return getOne(queryWrapper, false);
	}
}
