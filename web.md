# web开发

## 1、SpringMVC自动配置概览
Spring Boot provides auto-configuration for Spring MVC that works well with most applications.
The auto-configuration adds the following features on top of Spring’s defaults:
- Inclusion of ContentNegotiatingViewResolver and BeanNameViewResolver beans.
- Support for serving static resources, including support for WebJars (covered later in this document).
- Automatic registration of Converter, GenericConverter, and Formatter beans.
- Support for HttpMessageConverters (covered later in this document).
- Automatic registration of MessageCodesResolver (covered later in this document).
- Static index.html support.
- Automatic use of a ConfigurableWebBindingInitializer bean (covered later in this document).

## 2、简单功能分析
### 2.1、静态资源访问
#### 1、静态资源目录
called /static (or /public or /resources or /META-INF/resources)
It uses the ResourceHttpRequestHandler from Spring MVC so that you can modify that behavior by adding your own WebMvcConfigurer and overriding the addResourceHandlers method.
访问：当前项目根路径/ + 静态资源名

原理：静态映射/**
请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。静态资源也找不到404

```yaml
spring.mvc.static-path-pattern=/resources/**
```
### 2.4 静态资源配置原理
- SpringBoot启动默认加载xxxAutoConfiguration类（自动配置类）
- SpringMVC功能的自动配置类WebMvcAutoConfiguration生效
```java
@AutoConfiguration(after = { DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@ImportRuntimeHints(WebResourcesRuntimeHints.class)
public class WebMvcAutoConfiguration {}
```
- 给容器中配置了什么
```java
@Configuration(proxyBeanMethods = false)
	@Import(EnableWebMvcConfiguration.class)
	@EnableConfigurationProperties({ WebMvcProperties.class, WebProperties.class })
	@Order(0)
	public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ServletContextAware {}
```
- 配置文件的相关属性和xxx进行了绑定。WebMvcProperties==spring.mvc、WebProperties==spring.web

配置类只有一个有参构造器
```java
// 有参构造器所有参数值都会从容器中确定
// WebProperties webProperties 获取和spring.web绑定的所有的值的对象
// WebMvcProperties mvcProperties 获取和spring.mvc绑定的所有的值的对象
// ListableBeanFactory beanFactory Spring的beanFactory
// HttpMessageConverters 找到所有的HttpMessageConverters
// ResourceHandlerRegistrationCustomizer 找到资源处理器的自定义器
// DispatcherServletPath
// ServletRegistrationBean 给应用注册Servlet、Filter...
public WebMvcAutoConfigurationAdapter(WebProperties webProperties, WebMvcProperties mvcProperties,
            ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider,
            ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider,
            ObjectProvider<DispatcherServletPath> dispatcherServletPath,
            ObjectProvider<ServletRegistrationBean<?>> servletRegistrations) {
        this.resourceProperties = webProperties.getResources();
        this.mvcProperties = mvcProperties;
        this.beanFactory = beanFactory;
        this.messageConvertersProvider = messageConvertersProvider;
        this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
        this.dispatcherServletPath = dispatcherServletPath;
        this.servletRegistrations = servletRegistrations;
    }
```

禁用所有静态资源
```yaml
spring:
  web:
    resources:
      add-mappings: false
```
```java
// org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.addResourceHandlers
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!this.resourceProperties.isAddMappings()) {
        logger.debug("Default resource handling disabled");
        return;
    }
}
```

欢迎页的处理规则
```java
HandlerMapping：处理器映射，保存了一个Handler能处理那些请求
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext,
    FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
    WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(
    new TemplateAvailabilityProviders(applicationContext), applicationContext, getWelcomePage(),
    this.mvcProperties.getStaticPathPattern());
    welcomePageHandlerMapping.setInterceptors(getInterceptors(mvcConversionService, mvcResourceUrlProvider));
    welcomePageHandlerMapping.setCorsConfigurations(getCorsConfigurations());
    return welcomePageHandlerMapping;
}

WelcomePageHandlerMapping(TemplateAvailabilityProviders templateAvailabilityProviders,
        ApplicationContext applicationContext, Resource welcomePage, String staticPathPattern) {
    // 要使用欢迎页，必须是/**
    if (welcomePage != null && "/**".equals(staticPathPattern)) {
        logger.info("Adding welcome page: " + welcomePage);
        setRootViewName("forward:index.html");
    }
    else if (welcomeTemplateExists(templateAvailabilityProviders, applicationContext)) {
        // 调用Controller /index
        logger.info("Adding welcome page template: index");
        setRootViewName("index");
    }
}
```

## 3、请求参数处理
### 0、请求映射
- @xxxMapping
- Rest风格支持（使用HTTP请求方式动词来表示对资源的操作）
  - 以前：/getUser 获取用户 /deleteUser 删除用户 /editUser 修改用户 /saveUser 保存用户
  - 现在：/user GET 获取用户 DELETE 删除用户 PUT 修改用户 POST 保存用户
  - 核心Filter;HiddenHttpMethodFilter
    - 用法：表单method=post，隐藏域_method=put



Rest原理（表单提交要使用REST的时候）

- 表单提交会带上_method=PUT
- 请求狗来被HIddenHttpMethodFilter拦截
  - 请求是否正常，并且是POST
    - 获取到_method的值
    - 兼容一下请求：PUT/DELETE/PATCH
    - 原生request(post)，包装模式requestWrapper重写了getMethod方法，返回的是传入的值
    - 过滤链放行的时候使用的是wrapper。以后的方法调用getMethod是调用requstWrapper的

```HTML
<form action="/user" method="post">
  <input name="_method" type="hidden" value="PUT">
  <input value="REST-PUT 提交" type="submit">
</form>
```

```java
@PutMapping("/user")
public String putUser(){
    return "PUT 张三";
}
```

```java
@Bean
@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
@ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled", matchIfMissing = false)
public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
    return new OrderedHiddenHttpMethodFilter();
}
```

```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true # 开启页面表单的rest功能
```

```java
// org.springframework.web.filter.HiddenHttpMethodFilter#doFilterInternal
private static final List<String> ALLOWED_METHODS =
			List.of(HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name());

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    HttpServletRequest requestToUse = request;

    if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
        String paramValue = request.getParameter(this.methodParam);
        if (StringUtils.hasLength(paramValue)) {
            String method = paramValue.toUpperCase(Locale.ENGLISH);
            if (ALLOWED_METHODS.contains(method)) {
                requestToUse = new HttpMethodRequestWrapper(request, method);
            }
        }
    }

    filterChain.doFilter(requestToUse, response);
}
```

```java
private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

    private final String method;

    public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
        super(request);
        this.method = method;
    }

    @Override
    public String getMethod() {
        return this.method;
    }
}
```

Rest使用客户端工具

- 比如PostMan直接发送Put/Delete等方式，无需Filter

