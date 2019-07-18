package com.freedom.springcloud.gray.filter;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.zuul.ZuulFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

/**
 * 如果当前开启线程隔离模式，并且已经初始化过HystrixRequestContext
 * 在当前Filter中关闭HystrixRequestContext
 */
public class GrayPostZuulFilter extends ZuulFilter {

    @Autowired
    private ZuulProperties zuulProperties;

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2000;
    }

    @Override
    public boolean shouldFilter() {
        // 是否配置使用线程隔离模式
        if(zuulProperties.getRibbonIsolationStrategy() == HystrixCommandProperties.ExecutionIsolationStrategy.THREAD){
            // 是否已经初始化HystrixRequestContext
            return HystrixRequestContext.isCurrentThreadInitialized();
        }
        return false;
    }

    /**
     * 关闭HystrixRequestContext
     */
    private void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }

    @Override
    public Object run() {
        // 关闭HystrixRequestContext
        shutdownHystrixRequestContext();
        return null;
    }
}
