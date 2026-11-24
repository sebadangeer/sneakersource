package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.DTO.AgregarCarritoRequest;
import com.GestionSNKR.SnearksSource.model.Carrito;
import com.GestionSNKR.SnearksSource.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Carrito", description = "Operaciones del carrito asociado a clientes y sesiones")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @Operation(summary = "Consultar carrito de un cliente", description = "Obtiene el carrito persistente asociado al cliente. Crea o devuelve el carrito existente según la lógica del servicio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Identificador de cliente inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/clientes/{clienteId}/carrito")
    public ResponseEntity<Carrito> obtenerCarritoCliente(@Parameter(description = "Identificador del cliente propietario del carrito", example = "1", required = true) @PathVariable Long clienteId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(clienteId));
    }

    @Operation(summary = "Agregar producto al carrito del cliente", description = "Agrega una talla y cantidad al carrito persistente. Valida existencia del producto, disponibilidad de stock y datos solicitados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto agregado", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, talla o cantidad no válida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente o producto no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/clientes/{clienteId}/carrito/items")
    public ResponseEntity<?> agregarProductoCliente(
                @Parameter(description = "Identificador del cliente propietario del carrito", example = "1", required = true) @PathVariable Long clienteId,
            @RequestBody AgregarCarritoRequest request) {
        try {
            Carrito carrito = carritoService.agregarProducto(
                    clienteId,
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getTalla()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar cantidad en el carrito del cliente", description = "Actualiza la cantidad de una combinación producto-talla en el carrito persistente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cantidad no permitida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente, producto o elemento no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/api/clientes/{clienteId}/carrito/items/{productoId}")
    public ResponseEntity<?> actualizarCantidadCliente(
                @Parameter(description = "Identificador del cliente propietario del carrito", example = "1", required = true) @PathVariable Long clienteId,
                @Parameter(description = "Identificador del producto que se actualizará", example = "1", required = true) @PathVariable Long productoId,
            @RequestBody AgregarCarritoRequest request) {
        try {
            request.setProductoId(productoId);
            Carrito carrito = carritoService.actualizarCantidad(
                    clienteId,
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getTalla()
            );
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar producto del carrito del cliente", description = "Elimina una talla concreta de un producto del carrito persistente. Si no se indica talla, el servicio intenta eliminar el elemento según su comportamiento definido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente, producto o elemento no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/clientes/{clienteId}/carrito/items/{productoId}")
    public ResponseEntity<?> eliminarProductoCliente(
                @Parameter(description = "Identificador del cliente propietario del carrito", example = "1", required = true) @PathVariable Long clienteId,
                @Parameter(description = "Identificador del producto que se eliminará", example = "1", required = true) @PathVariable Long productoId,
                @Parameter(description = "Talla exacta a eliminar", example = "40", required = false) @RequestParam(required = false) String talla) {
        try {
            Carrito carrito = carritoService.eliminarProducto(clienteId, productoId, talla);
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Vaciar carrito del cliente", description = "Elimina todos los elementos del carrito persistente del cliente sin eliminar al cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito vaciado", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Identificador de cliente inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/clientes/{clienteId}/carrito")
    public ResponseEntity<Carrito> vaciarCarritoCliente(@Parameter(description = "Identificador del cliente propietario del carrito", example = "1", required = true) @PathVariable Long clienteId) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(clienteId));
    }

    @Operation(summary = "Consultar carrito de la sesión", description = "Obtiene el carrito temporal asociado a la sesión HTTP actual. No requiere un identificador de cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito obtenido", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/api/carrito")
    public ResponseEntity<Carrito> obtenerCarrito(HttpSession session) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(session));
    }

    @Operation(summary = "Agregar producto al carrito de sesión", description = "Agrega una talla y cantidad al carrito asociado a la sesión HTTP. Valida el producto, la talla y la disponibilidad.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto agregado", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, talla o cantidad no permitida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/api/carrito/items")
    public ResponseEntity<?> agregarProducto(@RequestBody AgregarCarritoRequest request, HttpSession session) {
        try {
            Carrito carrito = carritoService.agregarProducto(
                    session,
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getTalla()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar cantidad en el carrito de sesión", description = "Actualiza la cantidad de un producto-talla dentro del carrito asociado a la sesión HTTP.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cantidad no permitida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto o elemento no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/api/carrito/items/{productoId}")
    public ResponseEntity<?> actualizarCantidad(
                @Parameter(description = "Identificador del producto que se actualizará", example = "1", required = true) @PathVariable Long productoId,
            @RequestBody AgregarCarritoRequest request,
            HttpSession session) {
        try {
            request.setProductoId(productoId);
            Carrito carrito = carritoService.actualizarCantidad(
                    session,
                    request.getProductoId(),
                    request.getCantidad(),
                    request.getTalla()
            );
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar producto del carrito de sesión", description = "Elimina una talla concreta de un producto del carrito temporal de la sesión.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto o elemento no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/carrito/items/{productoId}")
    public ResponseEntity<?> eliminarProducto(
                @Parameter(description = "Identificador del producto que se eliminará", example = "1", required = true) @PathVariable Long productoId,
                @Parameter(description = "Talla exacta a eliminar", example = "40", required = false) @RequestParam(required = false) String talla,
            HttpSession session) {
        try {
            Carrito carrito = carritoService.eliminarProducto(session, productoId, talla);
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Vaciar carrito de sesión", description = "Elimina todos los elementos del carrito temporal asociado a la sesión HTTP.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito vaciado", content = @Content(schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/api/carrito")
    public ResponseEntity<Carrito> vaciarCarrito(HttpSession session) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(session));
    }
}