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
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.MemberSearchParam;
import com.kbopark.operation.dto.MerchantMemberPageDTO;
import com.kbopark.operation.dto.VipCardDTO;
import com.kbopark.operation.entity.MerchantMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商家会员
 *
 * @author laomst
 * @date 2020-08-31 15:16:27
 */
@Mapper
public interface MerchantMemberMapper extends BaseMapper<MerchantMember> {

    List<MerchantMemberPageDTO> selectMemberPage(IPage<MerchantMemberPageDTO> page, @Param("query") MemberSearchParam param);

	/**
	 * 会员卡分页列表查询
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<VipCardDTO> selectVipcardPage(IPage<VipCardDTO> page, @Param("query") VipCardDTO param);

	/**
	 * 查询某个商家近n个月的会员增长数量
	 * @param offsetList
	 * @param merchantId
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthCountGroup(@Param("offsetList") List<Integer> offsetList, @Param("merchantId") Integer merchantId);
}
