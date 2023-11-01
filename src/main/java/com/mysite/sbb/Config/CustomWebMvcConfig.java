package com.mysite.sbb.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**","/myimg/**")
                .addResourceLocations("File:///D:/2023 국비 김주현/웹개발/study/sbb/src/main/resources/static/files/","File:///D:/2023 국비 김주현/웹개발/study/sbb/src/main/resources/static/img/");
    }
}