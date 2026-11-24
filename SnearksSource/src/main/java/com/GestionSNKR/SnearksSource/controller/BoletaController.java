package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.DTO.CheckoutRequest;
import com.GestionSNKR.SnearksSource.model.Boleta;
import com.GestionSNKR.SnearksSource.service.BoletaService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Boletas", description = "Checkout, historial y resumen de ventas")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @Operation(summary = "Crear boleta desde el carrito", description = "Convierte el carrito del cliente en una boleta, registra dirección y método de pago y puede enviar el comprobante por correo.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Boleta creada"), @ApiResponse(responseCode = "400", description = "Carrito vacío o datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "404", description = "Cliente o producto inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno o envío de correo fallido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/clientes/{clienteId}/boletas")
    public ResponseEntity<?> crearBoleta(@Parameter(description = "Identificador del cliente", example = "1", required = true) @PathVariable Long clienteId, @RequestBody CheckoutRequest request) {
        try {
            Boleta boleta = boletaService.crearDesdeCarrito(clienteId, request.getMetodoPago(), request.getDireccion());
            return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar boletas administrativas", description = "Devuelve todas las boletas registradas. Acceso previsto para rol ADMIN.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Listado obtenido"), @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "403", description = "Rol insuficiente", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/clientes/admin/boletas")
    public ResponseEntity<List<Boleta>> listarBoletasAdmin() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @Operation(summary = "Listar boletas del cliente", description = "Devuelve el historial de boletas asociado al cliente indicado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Historial obtenido"), @ApiResponse(responseCode = "404", description = "Cliente inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/clientes/{clienteId}/boletas")
    public ResponseEntity<List<Boleta>> listarBoletas(@Parameter(description = "Identificador del cliente", example = "1", required = true) @PathVariable Long clienteId) {
        return ResponseEntity.ok(boletaService.listarPorCliente(clienteId));
    }

    @Operation(summary = "Listar todas las boletas", description = "Devuelve el listado global de ventas; operación de administración sin paginación.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Listado obtenido"), @ApiResponse(responseCode = "401", description = "No autenticado"), @ApiResponse(responseCode = "403", description = "Rol insuficiente")})
    @GetMapping("/boletas")
    public ResponseEntity<List<Boleta>> listarBoletasGlobales() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @Operation(summary = "Obtener resumen de ventas", description = "Calcula cantidad de boletas, ventas totales y devuelve el detalle agregado para el dashboard.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Resumen obtenido"), @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/boletas/resumen")
    public ResponseEntity<Map<String, Object>> resumenBoletas() {
        List<Boleta> boletas = boletaService.listarTodas();
        double totalVentas = boletas.stream()
                .mapToDouble(boleta -> boleta.getTotal() != null ? boleta.getTotal() : 0.0)
                .sum();

        Map<String, Object> payload = new HashMap<>();
        payload.put("cantidad", boletas.size());
        payload.put("totalVentas", totalVentas);
        payload.put("boletas", boletas);
        return ResponseEntity.ok(payload);
    }

    @Operation(summary = "Consultar boleta del cliente", description = "Obtiene una boleta si pertenece al cliente indicado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Boleta encontrada"), @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "404", description = "Boleta o cliente inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/clientes/{clienteId}/boletas/{boletaId}")
    public ResponseEntity<?> obtenerBoleta(@Parameter(description = "Identificador del cliente", example = "1", required = true) @PathVariable Long clienteId, @Parameter(description = "Identificador de la boleta", example = "10", required = true) @PathVariable Long boletaId) {
        try {
            Boleta b = boletaService.obtener(clienteId, boletaId);
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Consultar boleta global", description = "Obtiene una boleta por identificador desde el endpoint administrativo global.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Boleta encontrada"), @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "404", description = "Boleta inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "403", description = "Rol insuficiente")})
    @GetMapping("/boletas/{boletaId}")
    public ResponseEntity<?> obtenerBoletaGlobal(@Parameter(description = "Identificador de la boleta", example = "10", required = true) @PathVariable Long boletaId) {
        try {
            return ResponseEntity.ok(boletaService.obtenerPorId(boletaId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}