package io.github.tayluansantos.expense_tracker_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Expense Tracker API")
                        .description("API para gerenciamento de despesas pessoais, permitindo o cadastro, consulta, atualização e remoção de despesas e categorias. A autenticação é baseada em JWT para garantir segurança.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Tayluan de Jesus dos Santos")
                                .url("https://github.com/TayluanSantos/expense-tracker-api")
                                .email("tayluanjesus0298@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                .description("Github")
                .url("https://github.com/TayluanSantos"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

    }
}
