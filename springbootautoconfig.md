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
#### 3.1 142个自动配置类

#### 3.2按需开启自动配置项
```text
虽然我们142个场景的所有自动配置启动的时候默认全部加载。
但是不是全部加载，按条件装配规则（@Conditional）,最终会按需加载。
```

#### 3.3 修改默认配置
```java
@Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME) // 容器中有这个名字multipartResolver的组件
@ConditionalOnMissingBean(MultipartResolver.class) // 容器中没有这个类型组件
public StandardServletMultipartResolver multipartResolver() {
    StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
    multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
    return multipartResolver;
}
// 给容器中加入了文件上传解析器
@Bean标注的方法传入了对象参数，这个参数的值就会从容器中找
```

SpringBoot默认会在底层配置好所有的组件。但是如果用户自己配置了以用户的优先。

```java
@Bean
@ConditionalOnMissingBean
public CharacterEncodingFilter characterEncodingFilter(){
}
```

总结：
- SpringBoot先加载所有的自动配置类
- 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值。
- xxxProperties里面拿，xxxProperties和配置文件进行了绑定
- 生效的配置类就会给容器中装配很多组件
- 只要容器中有这些组件，先当与这些功能就有了
- 定制化配置
  - 用户直接自己@Bean替换底层的组件
  - 用户去看这个组件是获取的配置文件什么值就去修改

xxxAutoConfiguration ---> 组件 ----> xxxProperties里面拿值 ---> application.properties

### 3.4 最佳实践