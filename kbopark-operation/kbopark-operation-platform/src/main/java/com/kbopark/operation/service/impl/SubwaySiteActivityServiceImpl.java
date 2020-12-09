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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.entity.SubwaySiteActivity;
import com.kbopark.operation.mapper.SubwaySiteActivityMapper;
import com.kbopark.operation.service.SubwaySiteActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地铁站点活动设置
 *
 * @author pigx code generator
 * @date 2020-09-02 10:10:33
 */
@Service
public class SubwaySiteActivityServiceImpl extends ServiceImpl<SubwaySiteActivityMapper, SubwaySiteActivity> implements SubwaySiteActivityService {


	@Override
	public SubwaySiteActivity findBySiteCode(String siteCode) {
		if(StringUtils.isBlank(siteCode)){
			return null;
		}
		QueryWrapper<SubwaySiteActivity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(SubwaySiteActivity::getSiteCode,siteCode);
		List<SubwaySiteActivity> list = list(queryWrapper);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}
}
