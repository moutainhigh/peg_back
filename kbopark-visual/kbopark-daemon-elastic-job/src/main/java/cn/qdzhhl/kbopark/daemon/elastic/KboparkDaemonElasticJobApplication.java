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

package cn.qdzhhl.kbopark.daemon.elastic;

import cn.qdzhhl.kbopark.common.feign.annotation.EnableKboparkFeignClients;
import cn.qdzhhl.kbopark.common.job.annotation.EnableKboparkXxlJob;
import cn.qdzhhl.kbopark.common.security.annotation.EnableKboparkResourceServer;
import cn.qdzhhl.kbopark.common.swagger.annotation.EnableKboparkSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author kbopark
 * @date 2018/7/24 分布式任务调度模块
 */
@EnableKboparkXxlJob
@EnableKboparkSwagger2
@EnableKboparkFeignClients
@SpringCloudApplication
@EnableKboparkResourceServer
public class KboparkDaemonElasticJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(KboparkDaemonElasticJobApplication.class, args);
	}

}
