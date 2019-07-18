package com.freedom.springcloud.gray.config;

import com.freedom.springcloud.gray.ribbon.TagMetadataRule2;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;

// RibbonClients的默认配置，将默认的IRule实现类设置为TagMetadataRule2
//@Configuration
public class DefaultRibbonClientsConfiguration {

    @Value("${ribbon.client.name}")
    private String name = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Bean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, name)) {
            return this.propertiesFactory.get(IRule.class, config, name);
        }

        //TagMetadataRule rule = new TagMetadataRule();
        TagMetadataRule2 rule = new TagMetadataRule2();

        // 默认配置
        //DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        rule.initWithNiwsConfig(config);

        return rule;
    }

}
