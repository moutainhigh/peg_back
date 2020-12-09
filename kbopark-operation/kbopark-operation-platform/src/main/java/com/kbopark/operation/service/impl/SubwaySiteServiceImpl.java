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

import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.operation.entity.SubwayLine;
import com.kbopark.operation.entity.SubwaySite;
import com.kbopark.operation.entity.SubwaySiteActivity;
import com.kbopark.operation.mapper.SubwayLineMapper;
import com.kbopark.operation.mapper.SubwaySiteActivityMapper;
import com.kbopark.operation.mapper.SubwaySiteMapper;
import com.kbopark.operation.service.SubwaySiteService;
import com.kbopark.operation.util.OperationConstants;
import com.kbopark.operation.util.SubwayLineVO;
import com.kbopark.operation.util.SubwayUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author pigx code generator
 * @date 2020-08-27 11:28:52
 */
@Service
@AllArgsConstructor
public class SubwaySiteServiceImpl extends ServiceImpl<SubwaySiteMapper, SubwaySite> implements SubwaySiteService {

	private final SubwaySiteActivityMapper activityMapper;

	private final SubwayLineMapper subwayLineMapper;

	private final SubwaySiteMapper subwaySiteMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R deleteById(Integer id) {
		SubwaySite subwaySite = getById(id);
		activityMapper.delete(Wrappers.<SubwaySiteActivity>lambdaQuery().eq(SubwaySiteActivity::getSiteCode, subwaySite.getSiteCode()));
		return R.ok(removeById(id));
	}


	@Override
	public R buildSubwayLineTree() {
		List<SubwayLine> subwayLines = subwayLineMapper.selectList(Wrappers.<SubwayLine>lambdaQuery().orderByAsc(SubwayLine::getSortNumber));
		List<SubwaySite> subwaySites = subwaySiteMapper.selectList(Wrappers.<SubwaySite>lambdaQuery().orderByAsc(SubwaySite::getSortNumber));
		if(CollectionUtils.isEmpty(subwayLines)){
			return R.ok(OperationConstants.SUBWAY_LINE_NO_DATA);
		}
		List<SubwayLineVO> subwayLineList = new ArrayList<>();
		for (SubwayLine subwayLine : subwayLines) {
			SubwayLineVO subwayLineVO = new SubwayLineVO();
			subwayLineVO.setId(subwayLine.getCode());
			subwayLineVO.setParentId("0");
			subwayLineVO.setCode(subwayLine.getCode());
			subwayLineVO.setText(subwayLine.getName());
			subwayLineList.add(subwayLineVO);
		}
		if(!CollectionUtils.isEmpty(subwaySites)){
			for (SubwaySite subwaySite : subwaySites) {
				SubwayLineVO subwayLineVO = new SubwayLineVO();
				subwayLineVO.setId(subwaySite.getSiteCode());
				subwayLineVO.setParentId(subwaySite.getLineCode());
				subwayLineVO.setCode(subwaySite.getSiteCode());
				subwayLineVO.setText(subwaySite.getName());
				String lng = StringUtils.isBlank(subwaySite.getLng()) ? "0" : subwaySite.getLng();
				String lat = StringUtils.isBlank(subwaySite.getLat()) ? "0" : subwaySite.getLat();
				subwayLineVO.setLng(Double.valueOf(lng));
				subwayLineVO.setLat(Double.valueOf(lat));
				subwayLineList.add(subwayLineVO);
			}
		}

		return R.ok(SubwayUtil.buildLineTree(subwayLineList,"0"));
	}


	@Override
	public SubwaySite findByLngAndLat(double lng, double lat) {
		QueryWrapper<SubwaySite> subwaySiteQueryWrapper = new QueryWrapper<>();
		subwaySiteQueryWrapper.lambda()
				.eq(SubwaySite::getLng, lng)
				.eq(SubwaySite::getLat, lat);
		return getOne(subwaySiteQueryWrapper,false);
	}
}
