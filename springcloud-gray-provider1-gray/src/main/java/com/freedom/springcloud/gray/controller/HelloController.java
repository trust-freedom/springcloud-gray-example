package com.freedom.springcloud.gray.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map hello(){
        Map result = new HashMap();
        result.put("msg", "This is provider1 gray");

        return result;
    }

}
