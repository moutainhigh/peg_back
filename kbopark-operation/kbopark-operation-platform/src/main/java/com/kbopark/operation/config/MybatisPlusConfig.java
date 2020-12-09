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

package com.kbopark.operation.config;

import cn.qdzhhl.kbopark.common.data.datascope.DataScopeHandle;
import cn.qdzhhl.kbopark.common.data.datascope.DataScopeInterceptor;
import cn.qdzhhl.kbopark.common.data.datascope.DataScopeSqlInjector;
import cn.qdzhhl.kbopark.common.data.datascope.KboparkDefaultDatascopeHandle;
import cn.qdzhhl.kbopark.common.data.tenant.KboparkTenantHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kbopark
 * @date 2020-02-08
 */
@Configuration
public class MybatisPlusConfig {


	/**
	 * 乐观锁插件
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		OptimisticLockerInterceptor optimisticLockerInterceptor = new OptimisticLockerInterceptor();
		return optimisticLockerInterceptor;
	}

}
