package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.DTO.GoogleLoginRequest;
import com.GestionSNKR.SnearksSource.DTO.GoogleLoginResponse;
import com.GestionSNKR.SnearksSource.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.GestionSNKR.SnearksSource.DTO.ErrorResponse;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Inicio de sesión mediante Google")
public class GoogleAuthController {
    private final GoogleAuthService googleAuthService;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Operation(summary = "Iniciar sesión con Google", description = "Valida el credential emitido por Google, crea o vincula el cliente y devuelve su identidad normalizada. No emite un JWT propio en esta implementación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación correcta", content = @Content(schema = @Schema(implementation = GoogleLoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credential ausente o inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "El credential de Google no pudo ser validado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Existe conflicto entre el Google Subject y otro cliente", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Google OAuth no está configurado o ocurrió un error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/google")
    public ResponseEntity<GoogleLoginResponse> googleLogin(@Parameter(description = "Credential generado por Google Identity Services", required = true) @RequestBody(required = false) GoogleLoginRequest request) {
        if (request == null || request.getCredential() == null
                || request.getCredential().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(googleAuthService.login(request.getCredential()));
    }
}