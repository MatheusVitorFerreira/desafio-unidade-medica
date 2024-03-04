package com.clinica_medica_Desafio.Config;


import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinica Medica Desafio API")  // Replace with your actual API title
                        .version("v1")
                        .description("Clinica Medica Desafio REST API")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org"))
                )
                .servers(Collections.singletonList(new Server().url("/clinica-medica-desafio"))); 
    }
}
