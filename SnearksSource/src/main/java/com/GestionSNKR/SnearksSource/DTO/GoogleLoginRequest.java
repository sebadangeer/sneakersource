package com.GestionSNKR.SnearksSource.DTO;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "GoogleLoginRequest", description = "Solicitud de autenticación con Google Identity Services")
public class GoogleLoginRequest {
    @Schema(description = "Credential JWT emitido por Google al iniciar sesión", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImV4YW1wbG8ifQ.eyJzdWIiOiIxMjM0NTYiLCJlbWFpbCI6InVzZXJpbkBleGFtcGxlLmNvbSJ9.firma", requiredMode = Schema.RequiredMode.REQUIRED)
    private String credential;
}
