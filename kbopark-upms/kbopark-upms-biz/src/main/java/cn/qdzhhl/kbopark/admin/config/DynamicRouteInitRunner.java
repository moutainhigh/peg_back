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

package cn.qdzhhl.kbopark.admin.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.qdzhhl.kbopark.admin.service.SysRouteConfService;
import cn.qdzhhl.kbopark.common.core.constant.CacheConstants;
import cn.qdzhhl.kbopark.common.gateway.support.DynamicRouteInitEvent;
import cn.qdzhhl.kbopark.common.gateway.vo.RouteDefinitionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;

/**
 * @author kbopark
 * @date 2018/10/31
 * <p>
 * 容器启动后保存配置文件里面的路由信息到Redis
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class DynamicRouteInitRunner {

	private final RedisTemplate redisTemplate;

	private final SysRouteConfService routeConfService;

	@Async
	@Order
	@EventListener({ WebServerInitializedEvent.class, DynamicRouteInitEvent.class })
	public void initRoute() {
		Boolean result = redisTemplate.delete(CacheConstants.ROUTE_KEY);
		log.info("初始化网关路由 {} ", result);

		routeConfService.list().forEach(route -> {
			RouteDefinitionVo vo = new RouteDefinitionVo();
			vo.setRouteName(route.getRouteName());
			vo.setId(route.getRouteId());
			vo.setUri(URI.create(route.getUri()));
			vo.setOrder(route.getOrder());

			JSONArray filterObj = JSONUtil.parseArray(route.getFilters());
			vo.setFilters(filterObj.toList(FilterDefinition.class));
			JSONArray predicateObj = JSONUtil.parseArray(route.getPredicates());
			vo.setPredicates(predicateObj.toList(PredicateDefinition.class));

			log.info("加载路由ID：{},{}", route.getRouteId(), vo);
			redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVo.class));
			redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, route.getRouteId(), vo);
		});
		log.debug("初始化网关路由结束 ");
	}

	/**
	 * redis 监听配置,监听 gateway_redis_route_reload_topic,重新加载Redis
	 * @param redisConnectionFactory redis 配置
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener((message, bytes) -> {
			log.warn("接收到重新Redis 重新加载路由事件");
			initRoute();
		}, new ChannelTopic(CacheConstants.ROUTE_REDIS_RELOAD_TOPIC));
		return container;
	}

}
