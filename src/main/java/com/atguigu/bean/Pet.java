package com.atguigu.bean;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/7 21:45
 * @since 0.0.1
 **/
public class Pet {
    private String name;

    public Pet() {
    }

    public Pet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                '}';
    }
}
