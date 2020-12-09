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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.CouponSearchParam;
import com.kbopark.operation.dto.MemberCouponDTO;
import com.kbopark.operation.entity.CouponReceive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券领取记录
 *
 * @author pigx code generator
 * @date 2020-09-01 09:35:43
 */
@Mapper
public interface CouponReceiveMapper extends BaseMapper<CouponReceive> {

	/**
	 * 用户卡券分页列表
	 * @param page
	 * @param param
	 * @return
	 */
    List<MemberCouponDTO> selectMemberCouponPage(Page<MemberCouponDTO> page, @Param("query") CouponSearchParam param);

	/**
	 * 用户卡券分页列表
	 * @param param
	 * @return
	 */
	List<MemberCouponDTO> selectMemberCouponList(@Param("query") CouponSearchParam param);

	/**
	 * 查询某个商家近n个月的卡券领取数量
	 * @param offsetList
	 * @param merchantId
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthCountGroup(@Param("offsetList") List<Integer> offsetList, @Param("merchantId") Integer merchantId);
}
