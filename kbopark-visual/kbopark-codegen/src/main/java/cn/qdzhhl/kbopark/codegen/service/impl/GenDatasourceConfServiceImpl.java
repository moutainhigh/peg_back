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
package cn.qdzhhl.kbopark.codegen.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qdzhhl.kbopark.codegen.entity.GenDatasourceConf;
import cn.qdzhhl.kbopark.codegen.mapper.GenDatasourceConfMapper;
import cn.qdzhhl.kbopark.codegen.service.GenDatasourceConfService;
import cn.qdzhhl.kbopark.common.core.util.SpringContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据源表
 *
 * @author kbopark
 * @date 2019-03-31 16:00:20
 */
@Slf4j
@Service
@AllArgsConstructor
public class GenDatasourceConfServiceImpl extends ServiceImpl<GenDatasourceConfMapper, GenDatasourceConf>
		implements GenDatasourceConfService {

	private final StringEncryptor stringEncryptor;

	private final DataSourceCreator dataSourceCreator;

	/**
	 * 保存数据源并且加密
	 * @param conf
	 * @return
	 */
	@Override
	public Boolean saveDsByEnc(GenDatasourceConf conf) {
		// 校验配置合法性
		if (!checkDataSource(conf)) {
			return Boolean.FALSE;
		}

		// 添加动态数据源
		addDynamicDataSource(conf);

		// 更新数据库配置
		conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
		this.baseMapper.insert(conf);
		return Boolean.TRUE;
	}

	/**
	 * 更新数据源
	 * @param conf 数据源信息
	 * @return
	 */
	@Override
	public Boolean updateDsByEnc(GenDatasourceConf conf) {
		if (!checkDataSource(conf)) {
			return Boolean.FALSE;
		}
		// 先移除
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.removeDataSource(baseMapper.selectById(conf.getId()).getName());

		// 再添加
		addDynamicDataSource(conf);

		// 更新数据库配置
		if (StrUtil.isNotBlank(conf.getPassword())) {
			conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
		}
		this.baseMapper.updateById(conf);
		return Boolean.TRUE;
	}

	/**
	 * 通过数据源名称删除
	 * @param dsId 数据源ID
	 * @return
	 */
	@Override
	public Boolean removeByDsId(Integer dsId) {
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.removeDataSource(baseMapper.selectById(dsId).getName());
		this.baseMapper.deleteById(dsId);
		return Boolean.TRUE;
	}

	/**
	 * 添加动态数据源
	 * @param conf 数据源信息
	 */
	@Override
	public void addDynamicDataSource(GenDatasourceConf conf) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPollName(conf.getName());
		dataSourceProperty.setUrl(conf.getUrl());
		dataSourceProperty.setUsername(conf.getUsername());
		dataSourceProperty.setPassword(conf.getPassword());
		DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);

		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPollName(), dataSource);
	}

	/**
	 * 校验数据源配置是否有效
	 * @param conf 数据源信息
	 * @return 有效/无效
	 */
	@Override
	public Boolean checkDataSource(GenDatasourceConf conf) {
		try (Connection connection = DriverManager.getConnection(conf.getUrl(), conf.getUsername(),
				conf.getPassword())) {
		}
		catch (SQLException e) {
			log.error("数据源配置 {} , 获取链接失败", conf.getName(), e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
