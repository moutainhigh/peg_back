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
package cn.qdzhhl.kbopark.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qdzhhl.kbopark.mp.entity.WxAccountFans;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信公众号粉丝
 *
 * @author kbopark
 * @date 2019-03-26 22:08:08
 */
@Mapper
public interface WxAccountFansMapper extends BaseMapper<WxAccountFans> {

}
