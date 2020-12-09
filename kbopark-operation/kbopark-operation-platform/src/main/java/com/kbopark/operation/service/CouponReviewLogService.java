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

import cn.qdzhhl.kbopark.common.data.mybatis.KbBaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.apidto.ReviewLog;
import com.kbopark.operation.entity.CouponReviewLog;

import java.util.List;
import java.util.Optional;

/**
 * 优惠券审核记录
 *
 * @author pigx code generator
 * @date 2020-08-30 14:50:54
 */
public interface CouponReviewLogService extends KbBaseService<CouponReviewLog> {

	Optional<CouponReviewLog> getLastLogOfCoupon(Integer couponId);

	/**
	 *
	 * @param couponId
	 * @return
	 */
	List<ReviewLog> getReviewProgress(Integer couponId);

}
