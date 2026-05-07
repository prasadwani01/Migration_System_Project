package com.sample_project.migration_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // The addCorsMappings method has been completely removed to prevent
    // conflicts with SecurityConfig.java

}