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
import com.kbopark.operation.dto.CouponQueryDTO;
import com.kbopark.operation.dto.MemberCouponDTO;
import com.kbopark.operation.entity.CouponInfo;
import com.kbopark.operation.util.PositionQ;
import com.kbopark.operation.vo.CouponInfoVO;
import com.kbopark.operation.vo.MerchantCouponVO;
import com.kbopark.operation.vo.MerchantInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券表
 *
 * @author pigx code generator
 * @date 2020-08-28 16:15:36
 */
@Mapper
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {


	/**
	 * 根据经纬度查询优惠券列表【用户端】
	 *
	 * @param query 查询条件
	 * @return
	 */
	List<CouponInfoVO> getCouponPage(Page<CouponInfoVO> page, @Param("query") CouponQueryDTO query, @Param("memberPhone") String memberPhone);

	/**
	 * 根据经纬度查询商家列表【用户端】
	 *
	 * @param query 行业类别
	 * @return
	 */
	List<MerchantInfoVO> getMerchantList(@Param("query") CouponQueryDTO query);

	List<MerchantCouponVO> selectMemberMerchantCouponPage(Page<MerchantCouponVO> page,
														  @Param("type") String type,
														  @Param("merchantId") Integer merchantId,
														  @Param("memberPhone") String memberPhone);

	/**
	 * 获取用户可以领取的卡券列表
	 *
	 * @return
	 */
	List<MerchantCouponVO> getCanReceiveList(@Param("ids") List<Integer> ids, @Param("memberPhone") String memberPhone, @Param("merchantId") Integer merchantId);
}
