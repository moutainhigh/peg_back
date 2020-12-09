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
package com.kbopark.operation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.apidto.MonthNumberStatistics;
import com.kbopark.operation.dto.DtMemberDTO;
import com.kbopark.operation.dto.MemberSearchParam;
import com.kbopark.operation.dto.MerchantMemberPageDTO;
import com.kbopark.operation.dto.VipCardDTO;
import com.kbopark.operation.entity.MemberInfo;
import com.kbopark.operation.entity.Merchant;
import com.kbopark.operation.entity.MerchantMember;
import com.kbopark.operation.mapper.MerchantMemberMapper;
import com.kbopark.operation.service.MemberInfoService;
import com.kbopark.operation.service.MerchantMemberService;
import com.kbopark.operation.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商家会员
 *
 * @author laomst
 * @date 2020-08-31 15:16:27
 */
@Service
public class MerchantMemberServiceImpl extends ServiceImpl<MerchantMemberMapper, MerchantMember> implements MerchantMemberService {

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private MerchantService merchantService;

	@Override
	public IPage<MerchantMemberPageDTO> selectMemberPage(IPage<MerchantMemberPageDTO> page, MemberSearchParam param) {
		return page.setRecords(baseMapper.selectMemberPage(page, param));
	}

	@Override
	public IPage<VipCardDTO> selectVipcardPage(IPage<VipCardDTO> page, VipCardDTO param) {
		return baseMapper.selectVipcardPage(page, param);
	}

	@Override
	@Transactional
	public MerchantMember getByDtAndMerchant(DtMemberDTO dtMemberDTO, Integer merchantId) throws RuntimeException {
		MemberInfo member = memberInfoService.getByDt(dtMemberDTO);
		Merchant merchant = merchantService.getById(merchantId);
		if (null == merchant) {
			throw new UnsupportedOperationException("系统中没有对应的商家");
		}
		MerchantMember one = getOne(w -> w.lambda().eq(MerchantMember::getMemberId, member.getId()).eq(MerchantMember::getMerchantId, merchantId));
		if (one == null) {
			one = new MerchantMember();
			one.setBalance(new BigDecimal(0));
			one.setDistributorId(merchant.getDistributorId());
			one.setOperatorId(merchant.getOperatorId());
			one.setMemberId(member.getId());
			one.setMerchantId(merchantId);
			save(one);
		}
		return one;
	}

	@Override
	public List<MonthNumberStatistics> lastMonthCountGroup(Integer merchantId, Integer monthNumber) {
		List<Integer> offsetList = Stream.iterate(0, pre -> pre + 1).limit(monthNumber).collect(Collectors.toList());
		return baseMapper.lastMonthCountGroup(offsetList, merchantId);
	}
}
