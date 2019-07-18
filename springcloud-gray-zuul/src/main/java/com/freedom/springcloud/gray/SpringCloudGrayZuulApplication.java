package com.freedom.springcloud.gray;

import com.freedom.springcloud.gray.config.DefaultRibbonClientsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
//@ComponentScan( excludeFilters = { @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes={DefaultRibbonClientsConfiguration.class})})
public class SpringCloudGrayZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGrayZuulApplication.class, args);
	}

}
