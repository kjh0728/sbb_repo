package com.mysite.sbb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    static String getGenFileDirPath()
    {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files\\";
    }

    @Bean
    static String getMyImgDirPath()
    {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\";
    }
}
