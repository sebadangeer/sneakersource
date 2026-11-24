package com.GestionSNKR.SnearksSource.DTO;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "LoginDTO", description = "Credenciales utilizadas por el inicio de sesión tradicional")
public class LoginDTO {
    @Schema(description = "Correo registrado del cliente", example = "usuario@snearksource.com", format = "email", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String email;

    @Schema(description = "Contraseña del cliente", example = "MiClaveSegura123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 100, format = "password")
    private String contrasena;
}