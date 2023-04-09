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