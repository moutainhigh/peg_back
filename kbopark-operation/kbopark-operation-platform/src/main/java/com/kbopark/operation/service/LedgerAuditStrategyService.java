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

import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.entity.LedgerAuditStrategy;

/**
 * 财务审核策略设置
 *
 * @author pigx code generator
 * @date 2020-10-15 16:15:31
 */
public interface LedgerAuditStrategyService extends IService<LedgerAuditStrategy> {

	/***
	 * 根据机构ID查询财务审核策略
	 * @param deptId
	 * @return
	 */
	LedgerAuditStrategy findByDeptId(Integer deptId);
}
