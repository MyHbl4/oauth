package com.example.oauth.config;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String SWAGGER_API_TITLE = "REST API - SHIRE";
    private static final String SWAGGER_API_DESCRIPTION = "SHIRE WEBSITE";
    private static final String SWAGGER_API_LICENSE = "License";
    private static final String SWAGGER_API_VERSION = "1.0";

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global",
            "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(SWAGGER_API_TITLE)
            .description(SWAGGER_API_DESCRIPTION)
            .license(SWAGGER_API_LICENSE)
            .version(SWAGGER_API_VERSION)
            .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()))
            .pathMapping("/")
            .select()
            .paths(PathSelectors.regex("/api.*"))
            .build();
    }

}
