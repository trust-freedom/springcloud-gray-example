package com.freedom.springcloud.gray.test.config1;

import com.freedom.springcloud.gray.test.dto.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentConfig {

    @Bean
    public Student s1(){
        return new Student("aaa");
    }

}
