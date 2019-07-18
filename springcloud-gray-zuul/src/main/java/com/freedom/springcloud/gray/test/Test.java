package com.freedom.springcloud.gray.test;

import com.freedom.springcloud.gray.test.config1.ParentConfig;
import com.freedom.springcloud.gray.test.config2.ChildConfig;
import com.freedom.springcloud.gray.test.dto.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    
    public static void main(String[] args){
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.register(ParentConfig.class);
        parentContext.refresh();

        Student a = parentContext.getBean(Student.class);
        System.out.println("=========="+a.getName());

        AnnotationConfigApplicationContext childContext = new AnnotationConfigApplicationContext();
        childContext.register(ChildConfig.class);
        childContext.setParent(parentContext);
        childContext.refresh();

        Student b = childContext.getBean(Student.class);
        System.out.println("=========="+b.getName());

    }

    
}
