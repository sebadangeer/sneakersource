package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.DTO.LoginDTO;
import com.GestionSNKR.SnearksSource.model.Cliente;
import com.GestionSNKR.SnearksSource.service.ClienteService;
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

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Registro, consulta y autenticación de clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) { this.service = service; }

    @Operation(summary = "Listar clientes", description = "Devuelve todos los clientes registrados. Operación administrativa; no aplica paginación.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Listado obtenido", content = @Content(schema = @Schema(implementation = Cliente.class))), @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public List<Cliente> all() { return service.findAll(); }

    @Operation(summary = "Consultar un cliente", description = "Busca un cliente por su identificador numérico.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente encontrado"), @ApiResponse(responseCode = "404", description = "Cliente inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> get(@Parameter(description = "Identificador único del cliente", example = "1", required = true) @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Registrar cliente", description = "Crea un cliente, cifra su contraseña si es necesaria y aplica el rol CLIENTE cuando no se indica uno. Puede generar el carrito asociado en operaciones posteriores.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente creado", content = @Content(schema = @Schema(implementation = Cliente.class))), @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "409", description = "Correo o Google Subject duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody Cliente c) {
        Cliente saved = service.save(c);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar cliente", description = "Reemplaza los datos de perfil del cliente identificado por id. También normaliza el rol y cifra una contraseña nueva si corresponde.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente actualizado", content = @Content(schema = @Schema(implementation = Cliente.class))), @ApiResponse(responseCode = "400", description = "Datos o identificador inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "404", description = "Cliente inexistente", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "409", description = "Conflicto de unicidad", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@Parameter(description = "Identificador único del cliente", example = "1", required = true) @PathVariable Long id, @RequestBody Cliente c) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        c.setId(id);
        Cliente updated = service.update(id, c);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina el cliente y sus relaciones persistidas según las reglas JPA configuradas.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Cliente eliminado"), @ApiResponse(responseCode = "404", description = "Cliente inexistente"), @ApiResponse(responseCode = "400", description = "Identificador inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Identificador único del cliente", example = "1", required = true) @PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Iniciar sesión local", description = "Valida correo y contraseña usando BCrypt cuando corresponda y devuelve el cliente autenticado. La implementación actual no devuelve token JWT.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Autenticación correcta", content = @Content(schema = @Schema(implementation = Cliente.class))), @ApiResponse(responseCode = "400", description = "Cuerpo inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return service.login(loginDTO.getEmail(), loginDTO.getContrasena())
                .<ResponseEntity<?>>map(cliente -> ResponseEntity.ok(cliente))
                .orElseGet(() -> ResponseEntity.status(401).body("Credenciales incorrectas o usuario no encontrado"));
    }
}