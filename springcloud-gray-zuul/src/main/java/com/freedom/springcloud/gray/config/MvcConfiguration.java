package com.freedom.springcloud.gray.config;

import com.freedom.springcloud.gray.mvc.GrayRequestContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// （暂不使用）不使用拦截器初始化HystrixRequestContext、GrayRequestContext
//@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GrayRequestContextInterceptor());
    }
}
