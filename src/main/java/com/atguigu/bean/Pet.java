package com.atguigu.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/7 21:45
 * @since 0.0.1
 **/
@Data
@NoArgsConstructor
public class Pet {
    private String name;
    private Double weight;

    public Pet(String name) {
        this.name = name;
    }
}
