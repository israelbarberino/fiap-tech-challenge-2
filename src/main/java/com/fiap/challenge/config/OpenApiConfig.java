package com.fiap.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Usuários")
                        .version("1.0.0")
                        .description("API REST para gerenciamento completo de usuários com autenticação e autorização")
                        .contact(new Contact()
                                .name("FIAP Tech Challenge")
                                .email("support@fiap.com")
                                .url("https://www.fiap.com.br"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.example.com")
                                .description("Servidor de Produção")
                ));
    }
}
