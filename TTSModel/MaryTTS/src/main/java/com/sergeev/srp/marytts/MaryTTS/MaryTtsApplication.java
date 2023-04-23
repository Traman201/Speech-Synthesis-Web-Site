package com.sergeev.srp.marytts.MaryTTS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = {GroovyTemplateAutoConfiguration.class})
public class MaryTtsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MaryTtsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MaryTtsApplication.class);
    }

}
