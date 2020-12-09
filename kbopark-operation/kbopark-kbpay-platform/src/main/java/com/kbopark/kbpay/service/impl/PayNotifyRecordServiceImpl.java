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
package com.kbopark.kbpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbopark.kbpay.entity.PayNotifyRecord;
import com.kbopark.kbpay.mapper.PayNotifyRecordMapper;
import com.kbopark.kbpay.service.PayNotifyRecordService;
import org.springframework.stereotype.Service;

/**
 * 回调记录
 *
 * @author laomst
 * @date 2020-09-04 10:24:32
 */
@Service
public class PayNotifyRecordServiceImpl extends ServiceImpl<PayNotifyRecordMapper, PayNotifyRecord> implements PayNotifyRecordService {

}
