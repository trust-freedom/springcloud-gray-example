package com.freedom.springcloud.gray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudGrayEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGrayEurekaApplication.class, args);
	}

}
