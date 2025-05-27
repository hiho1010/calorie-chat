package com.sku.caloriechat;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
    info = @Info(
        title = "CalorieChat API",
        description = "식단·칼로리 챗봇 백엔드 REST API 명세",
        version = "v1.0.0"
    )
)
@SpringBootApplication
public class CaloriechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaloriechatApplication.class, args);
    }

}
