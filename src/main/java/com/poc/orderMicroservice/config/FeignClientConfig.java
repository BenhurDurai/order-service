package com.poc.orderMicroservice.config;

/*
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = getCurrentRequestToken();
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        };
    }
    private String getCurrentRequestToken() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null){
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")){
                return authHeader.substring(7);
            }
        }
        throw new RuntimeException("Authorization token is missing or invalid");
    }
}
*/


import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignClientConfig {

    public static final ThreadLocal<String> AUTH_TOKEN = new ThreadLocal<>();

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = AUTH_TOKEN.get();
            if (token != null && !token.isBlank()) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            }
        };
    }
}
