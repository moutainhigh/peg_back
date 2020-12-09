package com.kbopark.kbpay.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/10/12 10:42
 **/
@Component
@Configuration
@Data
@RefreshScope
public class PayUrl {

	/**
	 * 申请退款接口
	 */
	@Value("${kbpayconfig.refund_url}")
	private String refundPostUrl;

//	public String refund_post_url_pro = "https://qr.chinaums.com/netpay-route-server/api/";

}
