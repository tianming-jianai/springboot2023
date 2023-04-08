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

#### 2、@Import(AutoConfigurationImportSelector.class)
```java
1、利用getAutoConfigurationEntry(annotationMetadata);给容器中批量导入一些组件
2、调用List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);获取到所有需要导入到容器中的配置类
3、利用List<String> configurations = ImportCandidates.load(AutoConfiguration.class, getBeanClassLoader()).getCandidates();得到所有组件
4、从META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports位置加载一个文件
    默认扫描我们当前系统里面所有META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports位置的文件
    spring-boot-autoconfigure-3.0.5.jar包里面有这个文件
```
#### 按需开启自动配置项
```text
虽然我们142个场景的所有自动配置启动的时候默认全部加载。
但是不是全部加载，按条件装配规则（@Conditional）,最终会按需加载。
```

