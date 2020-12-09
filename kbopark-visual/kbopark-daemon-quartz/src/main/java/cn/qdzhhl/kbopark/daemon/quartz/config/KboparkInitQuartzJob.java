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

package cn.qdzhhl.kbopark.daemon.quartz.config;

import cn.qdzhhl.kbopark.daemon.quartz.constants.KboparkQuartzEnum;
import cn.qdzhhl.kbopark.daemon.quartz.service.SysJobService;
import cn.qdzhhl.kbopark.daemon.quartz.util.TaskUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 郑健楠
 * <p>
 * 初始化加载定时任务
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class KboparkInitQuartzJob {

	private final SysJobService sysJobService;

	private final TaskUtil taskUtil;

	private final Scheduler scheduler;

	@Bean
	public void customize() {
		sysJobService.list().forEach(sysjob -> {
			if (KboparkQuartzEnum.JOB_STATUS_RELEASE.getType().equals(sysjob.getJobStatus())) {
				taskUtil.removeJob(sysjob, scheduler);
			}
			else if (KboparkQuartzEnum.JOB_STATUS_RUNNING.getType().equals(sysjob.getJobStatus())) {
				taskUtil.resumeJob(sysjob, scheduler);
			}
			else if (KboparkQuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(sysjob.getJobStatus())) {
				taskUtil.pauseJob(sysjob, scheduler);
			}
			else {
				taskUtil.removeJob(sysjob, scheduler);
			}
		});
	}

}