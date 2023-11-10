package com.mysite.sbb.Config;

import com.mysite.sbb.Service.PrincipalOauthUserService;
import lombok.Builder;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Builder
public class SecurityConfig {

    private static final String SOCIAL_LOGIN = "social_login";

    private PrincipalOauthUserService principalOauthUserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .formLogin((formLogin) -> formLogin
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/"))
                .oauth2Login((oauth2Login) -> oauth2Login
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/member/signup/social")
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(principalOauthUserService))
                )
                .logout((logout)->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))

        ;
        return http.build();
    }



    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}