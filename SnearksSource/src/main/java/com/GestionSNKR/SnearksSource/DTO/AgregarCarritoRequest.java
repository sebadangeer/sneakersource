package com.GestionSNKR.SnearksSource.DTO;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "AgregarCarritoRequest", description = "Producto, cantidad y talla que se agregan o modifican en el carrito")
public class AgregarCarritoRequest {
    @Schema(description = "Identificador del producto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", format = "int64")
    private Long productoId;

    @Schema(description = "Cantidad solicitada; debe ser mayor que cero", example = "2", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "99", format = "int32")
    private Integer cantidad;

    @Schema(description = "Talla disponible para el producto", example = "40", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"35", "36", "37", "38", "39", "40", "41", "42"})
    private String talla;
}
