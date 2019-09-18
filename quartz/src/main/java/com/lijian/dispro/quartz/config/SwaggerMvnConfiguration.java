//package com.lijian.dispro.quartz.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
///**
// * 解决 swagger-ui.html 访问路径 404 问题
// *
// * @author Levin
// */
//@Configuration
//public class SwaggerMvnConfiguration extends WebMvcConfigurationSupport {
//
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
//    }
//
//}