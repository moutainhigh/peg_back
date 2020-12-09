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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.dto.CouponSearchParam;
import com.kbopark.operation.dto.MerchantCouponUsedDTO;
import com.kbopark.operation.entity.CouponUsedLog;

/**
 * 优惠券使用记录
 *
 * @author pigx code generator
 * @date 2020-09-04 10:15:04
 */
public interface CouponUsedLogService extends IService<CouponUsedLog> {

	/**
	 * 商家核销记录分页列表查询
	 * @param couponUsedLogPage
	 * @param param
	 * @return
	 */
	IPage<MerchantCouponUsedDTO> selectMerchantUsedPage(Page<MerchantCouponUsedDTO> couponUsedLogPage, CouponSearchParam param);
}
