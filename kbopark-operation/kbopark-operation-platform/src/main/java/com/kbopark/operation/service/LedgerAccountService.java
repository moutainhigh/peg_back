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
import com.kbopark.operation.entity.LedgerAccount;

import java.util.List;

/**
 * 分账账户管理
 *
 * @author pigx code generator
 * @date 2020-09-29 10:24:33
 */
public interface LedgerAccountService extends IService<LedgerAccount> {


	/**
	 * 根据商家ID或运营商ID或平台ID查询账户配置
	 * @param deptId
	 * @return
	 */
	LedgerAccount findByDeptId(Integer deptId);

	/**
	 * 根据企业用户号查询信息
	 *
	 * @param merNo
	 * @return
	 */
	LedgerAccount findByMerNo(String merNo);

	/**
	 * 根据企业用户号查询信息（排除指定Id）
	 *
	 * @param merNo
	 * @param id
	 * @return
	 */
	LedgerAccount findByMerNo(String merNo, Integer id);


	/**
	 * 根据账户类型查询对应账户集合
	 *
	 * @param type
	 * @return
	 */
	List<LedgerAccount> findListByType(String type);
}
