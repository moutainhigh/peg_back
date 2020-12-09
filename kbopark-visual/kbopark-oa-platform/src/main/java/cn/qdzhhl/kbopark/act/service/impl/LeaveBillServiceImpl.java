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

package cn.qdzhhl.kbopark.act.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.act.entity.LeaveBill;
import cn.qdzhhl.kbopark.act.mapper.LeaveBillMapper;
import cn.qdzhhl.kbopark.act.service.LeaveBillService;
import org.springframework.stereotype.Service;

/**
 * @author kbopark
 * @date 2018-09-27
 */
@Service("leaveBillService")
public class LeaveBillServiceImpl extends ServiceImpl<LeaveBillMapper, LeaveBill> implements LeaveBillService {

}
