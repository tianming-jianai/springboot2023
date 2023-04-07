package com.atguigu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/7 20:57
 * @since 0.0.1
 **/
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle01() {
        return "Hello, Spring Boot 3!";
    }
}
