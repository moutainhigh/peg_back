package com.kbopark.kbpay.JPush.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.qdzhhl.kbopark.admin.api.feign.RemoteUserService;
import com.kbopark.kbpay.JPush.service.JPushService;
import com.kbopark.kbpay.config.KbJPushConfig;
import com.kbopark.operation.entity.ConsumerOrder;
import com.kbopark.operation.fegin.RemoteOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class JPushServiceImpl implements JPushService {

	private final KbJPushConfig pushConfig;

	private final JPushClient pushClient;

	private final RemoteUserService remoteUserService;

	private final RemoteOrderService remoteOrderService;

	@Override
	public void pushPaySuccessNotify(ConsumerOrder consumerOrder) {
		try {
			if (consumerOrder == null || consumerOrder.getMerchantId() == null) {
				return;
			}
			String memberPhone = consumerOrder.getMemberPhone();
			if (StrUtil.isBlank(memberPhone)) {
				memberPhone = "";
			}
			List<String> aliases = remoteUserService.notifyUsernamesOfMerchant(consumerOrder.getMerchantId()).getData();
			String content = "尾号" + StrUtil.subSufByLength(memberPhone, 4) + "的用户支付成功" + consumerOrder.getMoney()+"元";
			sendPushByAlias(content, aliases);
		} catch (Exception ex) {
			log.error("支付成功消息推送失败");
		}
	}

	@Override
	public void pushPaySuccessNotify(String bizNumber) {
		pushPaySuccessNotify(remoteOrderService.getByOrderNumber(bizNumber).getData());
	}

	public void sendPushByAlias(String content, List<String> aliases) {
		if (StrUtil.isBlank(content) || CollectionUtil.isEmpty(aliases)) {
			return;
		}
		try {
//			JPushClient pushClient = new JPushClient(pushConfig.getMaster_secret(), pushConfig.getApp_key(), null, ClientConfig.getInstance());
			PushResult pushResult = pushClient.sendPush(PushPayload.newBuilder()
					.setPlatform(Platform.all())
					.setAudience(Audience.alias(aliases))
					.setNotification(Notification.alert(content))
					.build());
			log.info("使用极光通过别名进行消息推送: " + pushResult);
		} catch (Exception ex) {
			log.error("使用极光进行消息推送失败: " + ex.getMessage());
		}
	}
}
