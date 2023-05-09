package com.sergeev.srp.site.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${synthesis.audio-path}")
    String filePath;

    @Value("${synthesis.audio-resource-handler}")
    String handler;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
        resourceHandlerRegistry.addResourceHandler(handler + "**")
                .addResourceLocations(filePath)
                .setCachePeriod(0);
    }
}