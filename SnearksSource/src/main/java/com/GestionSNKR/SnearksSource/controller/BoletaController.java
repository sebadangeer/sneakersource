package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.DTO.CheckoutRequest;
import com.GestionSNKR.SnearksSource.model.Boleta;
import com.GestionSNKR.SnearksSource.service.BoletaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @PostMapping("/clientes/{clienteId}/boletas")
    public ResponseEntity<?> crearBoleta(@PathVariable Long clienteId, @RequestBody CheckoutRequest request) {
        try {
            Boleta boleta = boletaService.crearDesdeCarrito(clienteId, request.getMetodoPago(), request.getDireccion());
            return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/clientes/admin/boletas")
    public ResponseEntity<List<Boleta>> listarBoletasAdmin() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @GetMapping("/clientes/{clienteId}/boletas")
    public ResponseEntity<List<Boleta>> listarBoletas(@PathVariable Long clienteId) {
        return ResponseEntity.ok(boletaService.listarPorCliente(clienteId));
    }

    @GetMapping("/boletas")
    public ResponseEntity<List<Boleta>> listarBoletasGlobales() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

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

    @GetMapping("/clientes/{clienteId}/boletas/{boletaId}")
    public ResponseEntity<?> obtenerBoleta(@PathVariable Long clienteId, @PathVariable Long boletaId) {
        try {
            Boleta b = boletaService.obtener(clienteId, boletaId);
            return ResponseEntity.ok(b);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/boletas/{boletaId}")
    public ResponseEntity<?> obtenerBoletaGlobal(@PathVariable Long boletaId) {
        try {
            return ResponseEntity.ok(boletaService.obtenerPorId(boletaId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
