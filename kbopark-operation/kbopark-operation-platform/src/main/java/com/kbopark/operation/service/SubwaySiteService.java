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

import cn.qdzhhl.kbopark.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.entity.SubwaySite;
import com.kbopark.operation.util.SubwaySiteTree;

import java.util.List;

/**
 * @author pigx code generator
 * @date 2020-08-27 11:28:52
 */
public interface SubwaySiteService extends IService<SubwaySite> {

	/**
	 * 根据站点ID删除站点信息并根据站点编号删除站点活动设置
	 *
	 * @param id
	 * @return
	 */
	R deleteById(Integer id);


	/**
	 * 获取地铁线路tree
	 *
	 * @return
	 */
	R buildSubwayLineTree();


	/**
	 * 根据经纬度查询站点信息
	 * @param lng
	 * @param lat
	 * @return
	 */
	SubwaySite findByLngAndLat(double lng, double lat);

}
