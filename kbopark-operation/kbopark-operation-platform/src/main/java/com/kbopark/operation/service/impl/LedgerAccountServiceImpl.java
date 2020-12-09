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
import com.kbopark.operation.entity.LedgerAccount;
import com.kbopark.operation.mapper.LedgerAccountMapper;
import com.kbopark.operation.service.LedgerAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分账账户管理
 *
 * @author pigx code generator
 * @date 2020-09-29 10:24:33
 */
@Service
public class LedgerAccountServiceImpl extends ServiceImpl<LedgerAccountMapper, LedgerAccount> implements LedgerAccountService {


	@Override
	public LedgerAccount findByDeptId(Integer deptId) {
		QueryWrapper<LedgerAccount> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAccount::getRelationId, deptId);
		return getOne(queryWrapper, false);
	}

	@Override
	public LedgerAccount findByMerNo(String merNo) {
		QueryWrapper<LedgerAccount> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAccount::getMerNo, merNo);
		return getOne(queryWrapper, false);
	}

	@Override
	public LedgerAccount findByMerNo(String merNo, Integer id) {
		QueryWrapper<LedgerAccount> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAccount::getMerNo, merNo).ne(LedgerAccount::getId, id);
		return getOne(queryWrapper, false);
	}


	@Override
	public List<LedgerAccount> findListByType(String type) {
		QueryWrapper<LedgerAccount> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(LedgerAccount::getBelongType, type);
		return list(queryWrapper);
	}
}
