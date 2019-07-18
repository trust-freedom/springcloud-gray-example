package com.freedom.springcloud.gray;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableApolloConfig
public class SpringCloudGrayProvider1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGrayProvider1Application.class, args);
	}

}
