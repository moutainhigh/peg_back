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

package com.kbopark.kbpay.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.kbpay.entity.SystemPayChannel;
import com.kbopark.operation.entity.Merchant;

import java.util.function.Consumer;

/**
 * 系统支付通道
 *
 * @author laomst
 * @date 2020-09-03 10:05:23
 */
public interface SystemPayChannelService extends IService<SystemPayChannel> {

	// 删除
	Boolean removeSystemPayChannel(Integer id);

	// 新增
	Boolean saveSystemPayChannel(SystemPayChannel systemPayChannel);

	// 修改
	Boolean updateSystemPayChannel(SystemPayChannel entity);

	// 启用或禁用
	Boolean upOrDown(Integer id);


	/*************** 模板工具方法 **************************/
	default SystemPayChannel getOne(Consumer<QueryWrapper<SystemPayChannel>> wrapperBuilder) {
		QueryWrapper<SystemPayChannel> query = Wrappers.<SystemPayChannel>query();
		if (null != wrapperBuilder) {
			wrapperBuilder.accept(query);
		}
		return getOne(query);
	}

}
