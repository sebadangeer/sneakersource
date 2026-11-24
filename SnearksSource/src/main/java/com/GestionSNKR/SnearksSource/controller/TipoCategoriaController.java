package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.model.TipoCategoria;
import com.GestionSNKR.SnearksSource.repository.TipoCategoriaRepository;
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
@RequestMapping("/api/tipos-categoria")
@Tag(name = "Tipos de categoría", description = "Gestión de tipos de categoría del catálogo")
public class TipoCategoriaController {

    private final TipoCategoriaRepository tipoCategoriaRepository;

    @Autowired
    public TipoCategoriaController(TipoCategoriaRepository tipoCategoriaRepository) {
        this.tipoCategoriaRepository = tipoCategoriaRepository;
    }

    @Operation(summary = "Listar tipos de categoría", description = "Devuelve todos los tipos de categoría disponibles.")
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
    public ResponseEntity<List<TipoCategoria>> listarTodas() {
        return ResponseEntity.ok(tipoCategoriaRepository.findAll());
    }

    @Operation(summary = "Consultar tipo de categoría", description = "Obtiene un tipo de categoría por identificador.")
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
    public ResponseEntity<TipoCategoria> obtenerPorId(@PathVariable Long id) {
        return tipoCategoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar tipo por nombre", description = "Busca un tipo de categoría sin distinguir mayúsculas y minúsculas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operación completada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado; no aplica mientras no haya filtros JWT activos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado por rol; autorización no implementada en la versión actual", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos o recurso duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoCategoria> obtenerPorNombre(@PathVariable String nombre) {
        return tipoCategoriaRepository.findByNombreIgnoreCase(nombre)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear tipo de categoría", description = "Crea un tipo si no existe otro con el mismo nombre.")
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
    public ResponseEntity<TipoCategoria> crear(@RequestBody TipoCategoria tipoCategoria) {
        return tipoCategoriaRepository.findByNombreIgnoreCase(tipoCategoria.getNombre())
                .map(existing -> new ResponseEntity<>(existing, HttpStatus.CONFLICT))
                .orElseGet(() -> new ResponseEntity<>(tipoCategoriaRepository.save(tipoCategoria), HttpStatus.CREATED));
    }

    @Operation(summary = "Actualizar tipo de categoría", description = "Actualiza los datos de un tipo existente.")
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
    public ResponseEntity<TipoCategoria> actualizar(@PathVariable Long id, @RequestBody TipoCategoria tipoCategoria) {
        return tipoCategoriaRepository.findById(id).map(actual -> {
            actual.setNombre(tipoCategoria.getNombre());
            actual.setDescripcion(tipoCategoria.getDescripcion());
            actual.setImagen(tipoCategoria.getImagen());
            return ResponseEntity.ok(tipoCategoriaRepository.save(actual));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar tipo de categoría", description = "Elimina permanentemente un tipo si existe.")
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (tipoCategoriaRepository.existsById(id)) {
            tipoCategoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}