package com.springfw03_jpa_talktalk.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://ec2-3-36-119-31.ap-northeast-2.compute.amazonaws.com",       // Nginx 기본 80포트 추가
                        "http://ec2-3-36-119-31.ap-northeast-2.compute.amazonaws.com:5173",
                        "http://yoonyesol-175698563.s3-website.ap-northeast-2.amazonaws.com/"
                        ) // front-end domain
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}