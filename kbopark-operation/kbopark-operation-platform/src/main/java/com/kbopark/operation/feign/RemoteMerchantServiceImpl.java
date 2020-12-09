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

package com.kbopark.operation.feign;

import cn.qdzhhl.kbopark.common.core.util.R;
import cn.qdzhhl.kbopark.common.security.annotation.Inner;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.fegin.RemoteMerchantService;
import com.kbopark.operation.service.MerchantService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商家基本信息表
 *
 * @author laomst
 * @date 2020-08-25 17:59:54
 */
@RestController
@AllArgsConstructor
public class RemoteMerchantServiceImpl implements RemoteMerchantService {

	private final MerchantService merchantService;

	/**
	 * 通过id查询商家基本信息表
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询", notes = "通过id查询")
	@GetMapping(GET_BY_ID)
	@Inner(value = false)
	public R<Merchant> getById(@PathVariable("id") Integer id) {
		Merchant entity = merchantService.getById(id);
		return R.ok(entity);
	}

}
