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

package com.kbopark.operation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kbopark.operation.entity.CouponReviewLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券审核记录
 *
 * @author pigx code generator
 * @date 2020-08-30 14:50:54
 */
@Mapper
public interface CouponReviewLogMapper extends BaseMapper<CouponReviewLog> {

}
