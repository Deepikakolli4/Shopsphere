package com.shopsphere.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shopSphereOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("ShopSphere E-Commerce API")
                                .version("1.0")
                                .description(
                                        "REST API documentation for the ShopSphere E-Commerce Backend"
                                )
                                .contact(
                                        new Contact()
                                                .name("Deepika Kolli")
                                )
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication")
                )
                .components(
                        new io.swagger.v3.oas.models.Components()
                                .addSecuritySchemes(
                                        "Bearer Authentication",
                                        new SecurityScheme()
                                                .name("Bearer Authentication")
                                                .type(
                                                        SecurityScheme.Type.HTTP
                                                )
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}