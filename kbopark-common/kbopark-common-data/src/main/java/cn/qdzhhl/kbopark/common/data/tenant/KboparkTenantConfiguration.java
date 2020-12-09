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

package cn.qdzhhl.kbopark.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @author kbopark
 * @date 2020/4/29
 * <p>
 * 租户信息拦截
 */
@Configuration
public class KboparkTenantConfiguration {

	@Bean
	public RequestInterceptor kboparkFeignTenantInterceptor() {
		return new KboparkFeignTenantInterceptor();
	}

	@Bean
	public ClientHttpRequestInterceptor kboparkTenantRequestInterceptor() {
		return new TenantRequestInterceptor();
	}

}
