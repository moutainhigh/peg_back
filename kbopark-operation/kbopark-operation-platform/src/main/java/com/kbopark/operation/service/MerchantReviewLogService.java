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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.apidto.ReviewLog;
import com.kbopark.operation.entity.MerchantReviewLog;

import java.util.List;
import java.util.function.Consumer;

/**
 * 商家审核记录
 *
 * @author laomst
 * @date 2020-08-28 23:33:55
 */
public interface MerchantReviewLogService extends IService<MerchantReviewLog> {

	/**
	 * 获取商家的最后一条审核记录
	 * @param merchantId
	 * @return
	 */
	MerchantReviewLog getLastOfMerchant(Integer merchantId);

	/**
	 * 获取审核进展
	 * @param merchantId
	 * @return
	 */
	List<ReviewLog> getReviewProgress(Integer merchantId);

	/********************************* 模板工具方法 **************************************/
	default MerchantReviewLog getOne(Consumer<QueryWrapper<MerchantReviewLog>> wrapperBuilder) {
		QueryWrapper<MerchantReviewLog> query = Wrappers.<MerchantReviewLog>query();
		if (null != wrapperBuilder) {
			wrapperBuilder.accept(query);
		}
		return getOne(query);
	}

	default Boolean update(Consumer<UpdateWrapper<MerchantReviewLog>> wrapperBuilder) {
		UpdateWrapper<MerchantReviewLog> query = Wrappers.<MerchantReviewLog>update();
		if (null != wrapperBuilder) {
			wrapperBuilder.accept(query);
		}
		return update(query);
	}

}
