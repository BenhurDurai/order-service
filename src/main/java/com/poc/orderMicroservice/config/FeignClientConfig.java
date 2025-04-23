package com.poc.orderMicroservice.config;

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

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            // Replace with your logic to fetch/generate the token
//            String token = "Bearer your_static_or_dynamic_token_here";
//            requestTemplate.header("Authorization", token);
//
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setBearerAuth(getCurrentRequestToken());
//            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
//        };
//    }
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
//            String token = getCurrentRequestToken();
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IlVzZXJNaWNyb3NlcnZpY2UiLCJpYXQiOjE3NDUzODA2ODEsImV4cCI6MTc0NTQxNjY4MX0.0wWgrnud5gJ3O9ixuSCKw8uRoAVbodQivrfAkrPfiBA";
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
