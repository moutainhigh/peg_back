/*
 *
 *      Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: kbopark
 *
 */

package cn.qdzhhl.kbopark.admin.mapper;

import cn.qdzhhl.kbopark.admin.api.entity.SysDept;
import cn.qdzhhl.kbopark.common.data.datascope.DataScopeMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author kbopark
 * @since 2018-01-20
 */
@Mapper
public interface SysDeptMapper extends DataScopeMapper<SysDept> {

}
