package com.supplytracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info().title("Digital Supply Chain Tracker API").version("1.0").description("API documentation for logistics and shipment tracking system")
        );
    }
}

// to use this  url u need to hit is : http://localhost:8080/swagger-ui.html
