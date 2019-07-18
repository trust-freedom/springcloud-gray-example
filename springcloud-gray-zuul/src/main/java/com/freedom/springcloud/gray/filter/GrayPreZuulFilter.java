package com.freedom.springcloud.gray.filter;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 负责初始化HystrixRequestContext、GrayRequestContext的前置过滤器
 */
public class GrayPreZuulFilter extends ZuulFilter {

    //@Autowired
    //private ZuulProperties zuulProperties;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -2;
    }

    // 为了统一信号量、线程池隔离获取请求中tag标签的方式，均需创建HystrixRequestContext
    //@Override
    //public boolean shouldFilter() {
    //    // 是否配置使用线程隔离模式
    //    return zuulProperties.getRibbonIsolationStrategy() == HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;
    //}

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 初始化HystrixRequestContext（new HystrixRequestContext()，并放入threadlocal）
     * 否则在使用HystrixRequestVariableDefault时报错
     */
    private void initHystrixRequestContext() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }

    /**
     * 初始化GrayRequestContext
     * 在HystrixRequestContext.state中初始化 HystrixRequestVariableDefault : new GrayRequestContext()
     */
    private void initCurrentRequestContext(){
        GrayRequestContext.initCurrentRequestContext();
    }

    @Override
    public Object run() {
        // 初始化HystrixRequestContext
        initHystrixRequestContext();

        // 初始化GrayRequestContext
        initCurrentRequestContext();

        return null;
    }

}
