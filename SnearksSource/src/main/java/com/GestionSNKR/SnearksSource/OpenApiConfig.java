package com.GestionSNKR.SnearksSource;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI snearkSourceOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Entorno de desarrollo local");
        Server stagingServer = new Server()
                .url("https://staging.snearksource.example")
                .description("Entorno de staging — reemplazar por la URL publicada");
        Server productionServer = new Server()
                .url("https://api.snearksource.example")
                .description("Entorno de producción — reemplazar por la URL publicada");

        String description = """
                API REST del comercio electrónico SnearkSource.

                ## Funcionalidades principales
                - Catálogo de productos, categorías y tipos de categoría.
                - Registro, consulta y autenticación de clientes.
                - Inicio de sesión con Google OAuth 2.0 y soporte documental para tokens Bearer/JWT.
                - Carrito persistente por cliente y carrito temporal asociado a sesión HTTP.
                - Checkout, historial de boletas, resumen de ventas y envíos de boleta por correo.
                - Administración de publicaciones del blog.

                ## Autenticación
                Google OAuth se utiliza mediante `POST /api/auth/google`, enviando el credential obtenido por Google Identity Services.
                Los flujos locales aceptan credenciales de cliente (`email` y `contrasena`) en `POST /api/clientes/login`.
                El proyecto no tiene actualmente una emisión de JWT propia ni filtros Bearer activos. El esquema Bearer documentado queda disponible para integraciones futuras y contratos compatibles.

                ## Convenciones
                - Los identificadores numéricos se representan como `Long`.
                - Las fechas y marcas temporales deben enviarse y recibir ISO-8601 (por ejemplo `2026-11-21T08:34:16-03:00`).
                - Las horas se expresan preferentemente en zona horaria regional; el servidor conserva la zona horaria configurada en Java.
                - La API no expone paginación actualmente. Las colecciones devuelven listas completas.
                - Los precios se representan como valores numéricos decimales sin separadores de miles.
                - Las respuestas exitosas usan códigos `200`, `201` o `204` según la operación.

                ## Reglas de negocio
                - Los clientes nuevos se crean con rol `CLIENTE` si no se proporciona otro rol válido.
                - Las tallas disponibles dependen del inventario de cada producto.
                - El checkout descuenta o valida existencias y registra la dirección y el método de pago.
                - Una boleta solo puede ser consultada por su cliente o desde los endpoints administrativos/globales.
                - Los recursos inexistentes responden `404`; entradas inválidas o conflictos responden `400`/`409`.
                """;

        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Token JWT Bearer. Actualmente Google OAuth y credenciales locales gestionan autenticación; incluya el JWT en los clientes preparados para validación futura.");

        return new OpenAPI()
                .info(new Info()
                        .title("SnearkSource API")
                        .description(description)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SnearkSource")
                                .email("minipokersbdos@gmail.com"))
                        .license(new License()
                                .name("Uso interno")
                                .url("https://github.com/sebadangeer/sneakersource")))
                .servers(List.of(localServer, stagingServer, productionServer))
                .components(new Components().addSecuritySchemes("bearerAuth", bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
