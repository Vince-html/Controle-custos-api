package com.bordado.controle_custo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(
                        new Info()
                                .title("Rest API - Controle de custos e mão de obra")
                                .description("Api construida para atender necessidade referentes a controle de custos e mão de obra, bem como gerar valor final do produto")
                                .contact(new Contact().name("Vicente Augusto"))
                                .version("v0.1")
                );
    }

    private SecurityScheme securityScheme () {
        return new SecurityScheme()
                .description("Insira um token válido!")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("security");
    }
}
