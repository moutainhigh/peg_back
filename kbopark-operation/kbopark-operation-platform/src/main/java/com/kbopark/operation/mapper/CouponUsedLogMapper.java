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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.dto.CouponSearchParam;
import com.kbopark.operation.dto.MerchantCouponUsedDTO;
import com.kbopark.operation.entity.CouponUsedLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券使用记录
 *
 * @author pigx code generator
 * @date 2020-09-04 10:15:04
 */
@Mapper
public interface CouponUsedLogMapper extends BaseMapper<CouponUsedLog> {

    IPage<MerchantCouponUsedDTO> selectMerchantUsedPage(Page<MerchantCouponUsedDTO> page, @Param("query") CouponSearchParam param);
}
