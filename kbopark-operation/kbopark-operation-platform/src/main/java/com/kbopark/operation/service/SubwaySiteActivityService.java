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

import com.baomidou.mybatisplus.extension.service.IService;
import com.kbopark.operation.entity.SubwaySiteActivity;

/**
 * 地铁站点活动设置
 *
 * @author pigx code generator
 * @date 2020-09-02 10:10:33
 */
public interface SubwaySiteActivityService extends IService<SubwaySiteActivity> {


	/**
	 * 通过站点编号查询站点活动设置信息
	 *
	 * @param siteCode
	 * @return
	 */
	SubwaySiteActivity findBySiteCode(String siteCode);

}
