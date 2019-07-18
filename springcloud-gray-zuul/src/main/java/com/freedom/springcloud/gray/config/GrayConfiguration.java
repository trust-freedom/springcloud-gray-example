package com.freedom.springcloud.gray.config;

import com.freedom.springcloud.gray.filter.ExtractTagFilter;
import com.freedom.springcloud.gray.filter.GrayPostZuulFilter;
import com.freedom.springcloud.gray.filter.GrayPreZuulFilter;
import com.freedom.springcloud.gray.ribbon.TagMetadataRule;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.zuul.ZuulFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Configuration
@RibbonClients( defaultConfiguration = { DefaultRibbonClientsConfiguration.class } )
public class GrayConfiguration {

    /**
     * 提取请求头中的标签
     * @return
     */
    @Bean
    public ZuulFilter extractTagFilter(){
        return new ExtractTagFilter();
    }

    @Bean
    public ZuulFilter grayPreZuulFilter(){
        return new GrayPreZuulFilter();
    }

    @Bean
    public ZuulFilter grayPostZuulFilter(){
        return new GrayPostZuulFilter();
    }


    /**
     * 替换默认的IRule规则：ZoneAvoidanceRule
     * @return
     */
    //@Bean
    //public IRule ribbonRule() {
    //    TagMetadataRule rule = new TagMetadataRule();
    //
    //    // 默认配置
    //    DefaultClientConfigImpl config = new DefaultClientConfigImpl();
    //    rule.initWithNiwsConfig(config);
    //
    //    return rule;
    //}


}
