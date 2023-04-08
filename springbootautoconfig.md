# 自动配置原理入门

## 引导加载自动配置
```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {}
```

### 1、@SpringBootConfiguration
@Configuration：代表当前是一个配置类

### 2、@ComponentScan
指定扫描那些，看Spring注解开发

### 3、@EnableAutoConfiguration
```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {}
```
#### 1、@AutoConfigurationPackage
自动配置包？指定了默认规则
```java
@Import(AutoConfigurationPackages.Registrar.class) // 给容器中导入一个组件
public @interface AutoConfigurationPackage {}

// 利用Registrar给容器中导入一系列组件
// 将指定一个包下所有组件导入进来？Springboot2023Application所在包下
```



