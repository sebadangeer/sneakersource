package com.GestionSNKR.SnearksSource.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Respuesta uniforme utilizada por el manejador global de errores")
public record ErrorResponse(
        @Schema(description = "Descripción técnica breve del error", example = "No autorizado")
        String error
) {
}
