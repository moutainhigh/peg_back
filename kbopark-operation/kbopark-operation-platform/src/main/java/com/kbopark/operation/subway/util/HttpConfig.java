package com.kbopark.operation.subway.util;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/11 15:56
 **/
@Component
@Configuration
@Data
@RefreshScope
public class HttpConfig{

	@Value("${subwayconfig.url}")
	private String remoteUrl;

	/***生产地址**/
	public static final String PRO_URL = "http://apppay.qd-metro.com:8871/coupon/sendOut";

	/***测试地址**/
	public static final String TEST_URL = "http://58.56.166.170:8871/coupon/sendOut";


}
