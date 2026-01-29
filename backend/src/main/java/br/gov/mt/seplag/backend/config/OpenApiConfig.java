package br.gov.mt.seplag.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Solicitações - SEPLAG MT")
                .description("API REST para gerenciamento de solicitações administrativas")
                .version("1.0.0")
            );
    }
}
