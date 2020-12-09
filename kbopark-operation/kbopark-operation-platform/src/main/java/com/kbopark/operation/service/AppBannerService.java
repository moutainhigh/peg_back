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
import com.kbopark.operation.apidto.AppBannerListDTO;
import com.kbopark.operation.entity.AppBanner;

/**
 * 用户端首页图片关联
 *
 * @author laomst
 * @date 2020-09-30 11:38:14
 */
public interface AppBannerService extends KbBaseService<AppBanner> {

	/**
	 * 获取分组的列表
	 * @return
	 */
	AppBannerListDTO getGroupedBannerList();

}
