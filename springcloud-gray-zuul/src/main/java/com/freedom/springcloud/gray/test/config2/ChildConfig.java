package com.freedom.springcloud.gray.test.config2;

import com.freedom.springcloud.gray.test.dto.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChildConfig {

    @Bean
    @ConditionalOnMissingBean
    public Student s2(){
        return new Student("bbb");
    }

}
