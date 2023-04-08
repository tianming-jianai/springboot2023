package com.atguigu;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.atguigu.bean.Pet;
import com.atguigu.bean.User;
import com.atguigu.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.Stream;

/**
 * @author zhangshigang
 */
@SpringBootApplication
public class Springboot2023Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Springboot2023Application.class, args);
        String[] names = run.getBeanDefinitionNames();
        Stream.of(names).forEach(System.out::println);

//        User user = (User) run.getBean("user");
//        Pet tomcat = (Pet) run.getBean("tomcat");
//        Pet tomcat2 = (Pet) run.getBean("tomcat");
//        System.out.println(user);
//        System.out.println(tomcat);
//
//        MyConfig myConfig = run.getBean(MyConfig.class);
//        System.out.println(myConfig);
//        // 如果@Configuration(proxyBeanMethods = true)代理对象调用方法：SpringBoot总会检查这个组件是否在容器中有，保持组件单实例
//        User user1 = myConfig.user01();
//        User user2 = myConfig.user01();
//        System.out.println("user1 == user2：" + (user1 == user2));
//
//        System.out.println(user.getPet() == tomcat);
//
//        System.out.println("=".repeat(10));
//        String[] namesForType = run.getBeanNamesForType(User.class);
//        Stream.of(namesForType).forEach(System.out::println);
//
//        PatternLayoutEncoder bean = run.getBean(PatternLayoutEncoder.class);
//        System.out.println(bean);


        boolean tomcat = run.containsBean("tomcat");
        System.out.println("容器中tomcat组件：" + tomcat);

        boolean user = run.containsBean("user");
        System.out.println("容器中user组件：" + user);
    }

}
