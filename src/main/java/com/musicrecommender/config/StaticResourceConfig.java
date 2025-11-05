package com.musicrecommender.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "D:/MusicProject/uploads/"; // absolute path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(3600); // optional: cache 1 hour
    }
}
