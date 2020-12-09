package com.kbopark.kbpay.paysdk;

import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.paysdk.ums.service.KbUmsPayService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 工厂
 */
@Component
@Primary
public class KbPayServiceImpl implements IKbPayService<PayResult>{

	private static final Map<String, IKbPayService<? extends PayResult>> serviceMap = new HashMap<>();

	private final IKbPayService<PayResult> defaultService;

	{
		defaultService = new NoSuchChannelPayService(); // 注册默认的
		//注册银联
		serviceMap.put(PayChannelValueEnum.CHINA_UMS.code, KbUmsPayService.getInstance());
		serviceMap.put(PayChannelValueEnum.CHINA_H5.code, KbUmsPayService.getInstance());
	}

	@Override
	public PayResult jsPay(PayTradeOrder order) {
		return getService(order.getPayChannelValue()).jsPay(order);
	}

	@Override
	public PayResult createH5PayUrl(PayTradeOrder order) {
		return getService(order.getPayChannelValue()).createH5PayUrl(order);
	}

	@Override
	public String generatePayChannelOrderId(PayTradeOrder payTradeOrder) {
		return getService(payTradeOrder.getPayChannelValue()).generatePayChannelOrderId(payTradeOrder);
	}

	private IKbPayService<? extends PayResult> getService(String channelValue) {
		return serviceMap.getOrDefault(channelValue, defaultService);
	}
}
