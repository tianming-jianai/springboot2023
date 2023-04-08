package com.atguigu.config;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.atguigu.bean.Pet;
import com.atguigu.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author 张世罡
 * @version 0.0.1
 * @create 2023/4/7 21:49
 * @since 0.0.1
 * 1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
 * 2、配置类本身也是组件
 * 3、proxyBeanMethods：代理Bean的方法
 *      Full(proxyBeanMethods = true)【保证每个@Bean方法被调用多少此返回的组件都是单实例的】
 *      Lite(proxyBeanMethods = false)【每个@Bean方法被调用多少次返回的组件都是新创建的】
 *      解决问题：组件依赖必须使用Full模式。其他默认是否Lite模式
 *
 * 4、@Import({User.class, DBHelper.class})
 *      给容器中自动创建出这两个类型的组件、默认组件的名字就是全类名
 *
 **/
@Import({User.class, PatternLayoutEncoder.class})
@Configuration(proxyBeanMethods = true) // 告诉SpringBoot这是一个配置类
@ConditionalOnMissingBean(name = "tom")
@ImportResource("classpath:beans.xml")
public class MyConfig {

    /**
     * 外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象
     * @return
     */
//    @ConditionalOnBean(name = "tomcat")
    @Bean(name = "user") // 给容器中添加组件，返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01() {
        User user = new User("zhangsan", 18);
        // user组件依赖了pet组件
        user.setPet(tomcatPet());
        return user;
    }

    @Bean(name = "tomcat")
    public Pet tomcatPet() {
        return new Pet("tocmat");
    }
}
