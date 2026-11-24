package com.GestionSNKR.SnearksSource.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(name = "GoogleLoginResponse", description = "Identidad normalizada devuelta después de autenticar con Google")
public class GoogleLoginResponse {
    @Schema(description = "Identificador persistido del cliente", example = "1", format = "int64")
    private Long id;
    @Schema(description = "Primer nombre derivado del perfil de Google", example = "Sebastián")
    private String pnombre;
    @Schema(description = "Nombre completo asociado al cliente", example = "Sebastián Dangeer")
    private String nombreCompleto;
    @Schema(description = "Correo verificado por Google", example = "sebastian@snearksource.com", format = "email")
    private String email;
    @Schema(description = "Rol administrativo del cliente", example = "CLIENTE", allowableValues = {"CLIENTE", "ADMIN"})
    private String rol;
}
