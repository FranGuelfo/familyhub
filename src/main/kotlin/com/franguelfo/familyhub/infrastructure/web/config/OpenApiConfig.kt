package com.franguelfo.familyhub.infrastructure.web.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    private val securitySchemeName = "BearerAuth"

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("FamilyHub REST API")
                    .description("API para gestión familiar, tareas, vehículos y plazas de garaje con arquitectura hexagonal.")
                    .version("v1.0.0")
                    .contact(Contact().name("Fran Güelfo").email("fran@familyhub.com"))
            )
            // Aplica el esquema Bearer de forma global a todos los endpoints documentados
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Introduce el token JWT obtenido en el login (sin incluir el prefijo 'Bearer ')")
                )
            )
    }
}