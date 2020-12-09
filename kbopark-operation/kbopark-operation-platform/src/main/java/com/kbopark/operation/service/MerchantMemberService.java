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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.DtMemberDTO;
import com.kbopark.operation.dto.MemberSearchParam;
import com.kbopark.operation.dto.MerchantMemberPageDTO;
import com.kbopark.operation.dto.VipCardDTO;
import com.kbopark.operation.entity.MerchantMember;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商家会员
 *
 * @author laomst
 * @date 2020-08-31 15:16:27
 */
public interface MerchantMemberService extends KbBaseService<MerchantMember> {

	/**
	 * 自定义分页
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<MerchantMemberPageDTO> selectMemberPage(IPage<MerchantMemberPageDTO> page, MemberSearchParam param);

	/**
	 * 会员卡分页查询
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<VipCardDTO> selectVipcardPage(IPage<VipCardDTO> page, VipCardDTO param);

	@Transactional
	MerchantMember getByDtAndMerchant(DtMemberDTO dtMemberDTO, Integer MerchantId) throws RuntimeException;

	/**
	 * 查询某个商家近 n 个月的会员增长数量
	 * @param merchantId
	 * @param monthNumber
	 * @return
	 */
	List<MonthNumberStatistics> lastMonthCountGroup(Integer merchantId, Integer monthNumber);
}
