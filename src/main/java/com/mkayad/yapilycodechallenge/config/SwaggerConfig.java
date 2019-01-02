package com.mkayad.yapilycodechallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<String>(Arrays.asList("application/json",
                    "application/xml"));
    private static final Contact DEFAULT_CONTACT=new Contact("Moussa Kayad","","mkayad@gmail.com");
    private static final ApiInfo DEFAULT_API_INFO=new ApiInfo(
            "Yapily Code Challenge API",
            "Yapily Code Challenge API documentation",
            "1.0",
            "Terms of Service",
            DEFAULT_CONTACT,
            "Apache License Version 2.0",
            "https://www.apache.org/license.html",
            new ArrayList<>()
    );
    @Bean
    public Docket serviceAPI(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mkayad.yapilycodechallenge"))
                .paths(regex("/character.*"))
                .build()
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES);
    }

}
