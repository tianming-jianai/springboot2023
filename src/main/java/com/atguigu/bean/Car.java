package com.atguigu.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/8 17:04
 * @since 0.0.1
 * 只有在容器中，才能拥有SpringBoot提供的强大功能
 **/
@Data
//@Component
@ConfigurationProperties(prefix = "mycar")
public class Car {
    private String brand;
    private Integer price;
}
