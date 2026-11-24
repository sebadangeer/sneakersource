package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.model.Producto;
import com.GestionSNKR.SnearksSource.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.GestionSNKR.SnearksSource.DTO.ErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catálogo y gestión de zapatillas")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // 1. Obtener todos los productos (GET /api/productos)
    @Operation(summary = "Listar productos", description = "Devuelve el catálogo completo de zapatillas y su inventario por talla.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    // Endpoint genérico para obtener productos por nombre de tipo de categoría
    @Operation(summary = "Filtrar productos por tipo de categoría", description = "Devuelve productos cuyo tipo de categoría coincide con el valor recibido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/tipo-categoria/{nombre}")
    public ResponseEntity<List<Producto>> obtenerPorTipoCategoria(@Parameter(description = "Nombre del tipo de categoría", example = "Jordan", required = true) @PathVariable String nombre) {
            List<Producto> productos = productoService.obtenerPorTipoCategoria(nombre);
        return ResponseEntity.ok(productos);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/tipo-categoria/jordan")
    public ResponseEntity<List<Producto>> obtenerTipoJordan() {
            List<Producto> productos = productoService.obtenerPorTipoCategoria("jordan");
        return ResponseEntity.ok(productos);
    }
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/tipo-categoria/sports")
    public ResponseEntity<List<Producto>> obtenerTipoSports() {
            List<Producto> productos = productoService.obtenerPorTipoCategoria("Nike Sports");
        return ResponseEntity.ok(productos);
    }
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/tipo-categoria/urban")
    public ResponseEntity<List<Producto>> obtenerTipoUrban() {
            List<Producto> productos = productoService.obtenerPorTipoCategoria("Nike Urbano");
        return ResponseEntity.ok(productos);
    }

    // 2. Obtener un producto por ID (GET /api/productos/{id})
    @Operation(summary = "Consultar producto", description = "Obtiene el detalle e inventario de una zapatilla por ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@Parameter(description = "Identificador del producto", example = "1", required = true) @PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Crear un nuevo producto (POST /api/productos)
    @Operation(summary = "Crear producto", description = "Crea una zapatilla en el catálogo. Operación administrativa que modifica el inventario disponible.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recurso creado"),
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // 4. Actualizar un producto existente (PUT /api/productos/{id})
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos e inventario de una zapatilla existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@Parameter(description = "Identificador del producto", example = "1", required = true) @PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Eliminar un producto (DELETE /api/productos/{id})
    @Operation(summary = "Eliminar producto", description = "Elimina permanentemente una zapatilla del catálogo si existe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@Parameter(description = "Identificador del producto", example = "1", required = true) @PathVariable Long id) {
        if (productoService.eliminarProducto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}