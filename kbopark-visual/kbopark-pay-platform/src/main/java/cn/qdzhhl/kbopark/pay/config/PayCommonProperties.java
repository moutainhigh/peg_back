package cn.qdzhhl.kbopark.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author kbopark
 * @date 2019-06-18
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "kbopark.pay")
public class PayCommonProperties {

	/**
	 * 支付宝支付参数
	 */
	private AliPayConfig aliPayConfig;

	/**
	 * 微信支付参数
	 */
	private WxPayConfig wxPayConfig;

}