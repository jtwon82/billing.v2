package com.mobon.billing.dev.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Contact;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.swagger.controller")).paths(PathSelectors.any())
				.build();
//		.apiInfo(apiInfo());
	}

//	private ApiInfo apiInfo() {
//		return new ApiInfo(
//				"TEST API"
//				, "Some custom description of API."
//				, "0.0.1"
//				, "Terms of service"
//				, new Contact()
//				, "License of API"
//				, "API license URL"
//				, Collections.emptyList());
//	}
}
