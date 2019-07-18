package com.freedom.springcloud.gray.mvc;

import com.freedom.springcloud.gray.filter.GrayRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 暂不使用
 * HandlerInterceptorAdapter默认无法拦截到ZuulFilter
 */
public class GrayRequestContextInterceptor extends HandlerInterceptorAdapter {

    private void initHystrixRequestContext() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }

    private void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }

    private void initCurrentRequestContext(){
        GrayRequestContext.initCurrentRequestContext();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        initHystrixRequestContext();
        initCurrentRequestContext();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        shutdownHystrixRequestContext();
    }
}
