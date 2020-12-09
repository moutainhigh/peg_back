package cn.qdzhhl.kbopark.daemon.quartz;

import cn.qdzhhl.kbopark.common.feign.annotation.EnableKboparkFeignClients;
import cn.qdzhhl.kbopark.common.security.annotation.EnableKboparkResourceServer;
import cn.qdzhhl.kbopark.common.swagger.annotation.EnableKboparkSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author frwcloud
 * @date 2019/01/23 定时任务模块
 */
@EnableKboparkSwagger2
@EnableKboparkFeignClients
@SpringCloudApplication
@EnableKboparkResourceServer
public class KboparkDaemonQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(KboparkDaemonQuartzApplication.class, args);
	}

}
