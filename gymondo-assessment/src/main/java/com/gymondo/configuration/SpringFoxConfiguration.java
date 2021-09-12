package com.gymondo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringFoxConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalRequestParameters(Collections.singletonList(getApiKeyParameter()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gymondo"))
                .paths(PathSelectors.any())
                .build();
    }

    private RequestParameter getApiKeyParameter() {
        return new RequestParameterBuilder().name("API-KEY")
                .description("Api Key information to access APIs")
                .required(true)
                .in(ParameterType.HEADER)
                .example(new Example("a1111-b111c-d1111-1111e"))
                .build();
    }

}
