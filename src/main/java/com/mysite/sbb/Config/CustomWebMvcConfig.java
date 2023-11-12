package com.mysite.sbb.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String a = AppConfig.getGenFileDirPath();
        String b = "a";
        registry.addResourceHandler("/resources/**","/myimg/**")
                .addResourceLocations("File:///"+AppConfig.getGenFileDirPath(),"File:///" + AppConfig.getMyImgDirPath());
    }
}