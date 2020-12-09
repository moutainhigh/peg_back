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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.apidto.AppBannerListDTO;
import com.kbopark.operation.entity.AppBanner;
import com.kbopark.operation.mapper.AppBannerMapper;
import com.kbopark.operation.service.AppBannerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户端首页图片关联
 *
 * @author laomst
 * @date 2020-09-30 11:38:14
 */
@Service
public class AppBannerServiceImpl extends ServiceImpl<AppBannerMapper, AppBanner> implements AppBannerService {

	@Override
	public AppBannerListDTO getGroupedBannerList() {
		List<AppBanner> allList = list(w -> w.lambda().eq(AppBanner::getIsShow, Boolean.TRUE)
				.orderByAsc(AppBanner::getSort));
		Map<Integer, List<AppBanner>> groupedMap = allList.stream().collect(Collectors.groupingBy(AppBanner::getType));
		AppBannerListDTO resDto = new AppBannerListDTO();
		resDto.setBannerList(groupedMap.get(1));
		resDto.setRightImgList(groupedMap.get(2));
		return resDto;
	}
}
