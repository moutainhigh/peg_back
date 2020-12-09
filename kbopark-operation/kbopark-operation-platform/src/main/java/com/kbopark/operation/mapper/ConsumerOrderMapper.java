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
import com.kbopark.operation.dto.MerchantOrderSearchDTO;
import com.kbopark.operation.dto.MerchantPayOrderDTO;
import com.kbopark.operation.entity.ConsumerOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 消费订单
 *
 * @author pigx code generator
 * @date 2020-09-03 10:17:02
 */
@Mapper
public interface ConsumerOrderMapper extends BaseMapper<ConsumerOrder> {

    List<MerchantPayOrderDTO> selectMerchantOrderPage(Page<MerchantPayOrderDTO> page, @Param("query") MerchantOrderSearchDTO query);

	Double getMerchantIncomeMoney(@Param("merchantId") Integer merchantId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	/**
	 * 查询某个商家近n个月的订单数量
	 * @param offsetList
	 * @param merchantId
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthCountGroup(@Param("offsetList") List<Integer> offsetList, @Param("merchantId") Integer merchantId);

	/**
	 * 查询某个商家近n个月的收益金额
	 * @param offsetList
	 * @param merchantId
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthMoneyGroup(@Param("offsetList") List<Integer> offsetList, @Param("merchantId") Integer merchantId);
}
