package com.kbopark.kbpay.paynotifyhandler.service;

import cn.hutool.http.HttpUtil;
import com.kbopark.kbpay.JPush.service.JPushService;
import com.kbopark.kbpay.entity.PayNotifyRecord;
import com.kbopark.kbpay.entity.PayTradeOrder;
import com.kbopark.kbpay.paysdk.IPayNotifyHandler;
import com.kbopark.kbpay.paysdk.ums.handler.UmsKbNotifyHandler;
import com.kbopark.kbpay.service.PayNotifyRecordService;
import com.kbopark.kbpay.service.PayTradeOrderService;
import com.kbopark.operation.fegin.RemoteOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Slf4j
public class UmsNotifyHandleService {

	private final PayTradeOrderService payTradeOrderService;
	private final PayNotifyRecordService notifyRecordService;
	private final RemoteOrderService remoteOrderService;
	private final JPushService pushService;
	private static final ExecutorService pushTaskExecutor = Executors.newFixedThreadPool(4);

	@Transactional
	public String handle(String body) {
		Map<String, String> bodyMap = HttpUtil.decodeParamMap(body, StandardCharsets.UTF_8);
		PayTradeOrder order = payTradeOrderService.getOne(w -> w.lambda().eq(PayTradeOrder::getPayChannelOrderId, bodyMap.get("merOrderId")));
		if (null == order) {
			return "获取订单信息失败";
		}
		IPayNotifyHandler<Map<String, String>, String> handler = new UmsKbNotifyHandler(order, bodyMap).handle(bodyMap);
		// 更新状态
		PayTradeOrder tradeOrder = handler.getTradeOrder();
		// 保存记录
		PayNotifyRecord record = new PayNotifyRecord();
		record.setCreateTime(LocalDateTime.now());
		record.setRequestBody(body);
		record.setTradeBizNumber(tradeOrder.getTradeBizNumber());
		record.setResponseBody(handler.getReturnBody());
		notifyRecordService.save(record);
		// 通知业务系统
		if (handler.shouldNotifyBiz()) {
			remoteOrderService.notifySuccess(order.getTradeBizNumber());
		}
		payTradeOrderService.updateById(tradeOrder);
		// 向APP推送消息
		try {
			pushService.pushPaySuccessNotify(order.getTradeBizNumber());
		} catch (Exception ex) {
			log.error("===============> 向APP推送支付成功消息失败：" + ex.getMessage());
		}
		return handler.getReturnBody();
	}
}
