package com.atguigu.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/9 14:03
 * @since 0.0.1
 **/
@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Pet pet;
    private String interests;
    private List<String> animal;
    private Map<String, Object> score;
    private Set<Double> salarys;
    private Map<String, List<Pet>> allPets;
}
