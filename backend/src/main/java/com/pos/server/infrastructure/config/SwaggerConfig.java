package com.pos.server.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pos.server.web.controller"))
                .build().apiInfo(apiInfo());
    }

    public ApiInfo apiInfo (){
        return new ApiInfoBuilder().title("Pos Market").description("App de compras")
                .license("Apache Tomcat")
                .version("1.0")
                .build();
    }
}
