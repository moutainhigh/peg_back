package com.kbopark.kbpay.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Data
public class KbJPushConfig {
	@Value("${jpush.master_secret}")
	private String master_secret;
	@Value("${jpush.app_key}")
	private String app_key;

	@Bean
	public JPushClient jPushClient() {
		return new JPushClient(master_secret, app_key, null, ClientConfig.getInstance());
	}
}
