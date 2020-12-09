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

package cn.qdzhhl.kbopark.gateway;

import cn.qdzhhl.kbopark.common.gateway.annotation.EnableKboparkDynamicRoute;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author kbopark
 * @date 2018年06月21日 网关应用
 */
@EnableKboparkDynamicRoute
@SpringCloudApplication
public class KboparkGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(KboparkGatewayApplication.class, args);
	}

}
