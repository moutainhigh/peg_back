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

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.dto.DtMemberDTO;
import com.kbopark.operation.entity.MemberInfo;
import com.kbopark.operation.mapper.MemberInfoMapper;
import com.kbopark.operation.service.MemberInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员信息表
 *
 * @author laomst
 * @date 2020-08-31 14:50:53
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo> implements MemberInfoService {

	@Override
	public MemberInfo getByDt(DtMemberDTO dtMember) throws RuntimeException {
		MemberInfo one = getOne(w -> w.lambda().eq(MemberInfo::getPhone, dtMember.getPhone()));
		if (null == one) {
			one = new MemberInfo(dtMember);
		} else {
			one.updateByDt(dtMember);
		}
		saveOrUpdate(one);
		return one;
	}

	@Override
	public MemberInfo findByMobile(String mobile) {
		if(StringUtils.isBlank(mobile)){
			return null;
		}
		//避免出现重复数据，查询到集合返回第一个
		List<MemberInfo> list = list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getPhone, mobile));
		if(CollectionUtils.isEmpty(list)){
			return null;
		}else{
			return list.get(0);
		}
	}
}
