package com.GestionSNKR.SnearksSource.DTO;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "CheckoutRequest", description = "Datos necesarios para convertir el carrito en una boleta")
public class CheckoutRequest {
    @Schema(description = "Método de pago seleccionado", example = "WebPay", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"Efectivo", "Tarjeta", "WebPay", "Transferencia"})
    private String metodoPago;

    @Schema(description = "Dirección completa de entrega", example = "Av. Libertador Bernardo O'Higgins 1234, Santiago", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 5, maxLength = 200)
    private String direccion;
}
