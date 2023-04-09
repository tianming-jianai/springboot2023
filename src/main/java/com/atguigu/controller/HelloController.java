package com.atguigu.controller;

import com.atguigu.bean.Car;
import com.atguigu.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/7 20:57
 * @since 0.0.1
 **/
@RestController
public class HelloController {

    @Autowired
    Car car;

    @Autowired
    Person person;

    @RequestMapping("/person")
    public Person person() {
        System.out.println(person.getName());
        return person;
    }

    @RequestMapping("/car")
    public Car car() {
        return car;
    }

    @RequestMapping("/hello")
    public String handle01() {
        return "Hello, Spring Boot 3!";
    }
}
