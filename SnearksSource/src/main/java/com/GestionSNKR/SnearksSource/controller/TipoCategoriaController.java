package com.GestionSNKR.SnearksSource.controller;

import com.GestionSNKR.SnearksSource.model.TipoCategoria;
import com.GestionSNKR.SnearksSource.repository.TipoCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-categoria")
public class TipoCategoriaController {

    private final TipoCategoriaRepository tipoCategoriaRepository;

    @Autowired
    public TipoCategoriaController(TipoCategoriaRepository tipoCategoriaRepository) {
        this.tipoCategoriaRepository = tipoCategoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<TipoCategoria>> listarTodas() {
        return ResponseEntity.ok(tipoCategoriaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCategoria> obtenerPorId(@PathVariable Long id) {
        return tipoCategoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoCategoria> obtenerPorNombre(@PathVariable String nombre) {
        return tipoCategoriaRepository.findByNombreIgnoreCase(nombre)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoCategoria> crear(@RequestBody TipoCategoria tipoCategoria) {
        return tipoCategoriaRepository.findByNombreIgnoreCase(tipoCategoria.getNombre())
                .map(existing -> new ResponseEntity<>(existing, HttpStatus.CONFLICT))
                .orElseGet(() -> new ResponseEntity<>(tipoCategoriaRepository.save(tipoCategoria), HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCategoria> actualizar(@PathVariable Long id, @RequestBody TipoCategoria tipoCategoria) {
        return tipoCategoriaRepository.findById(id).map(actual -> {
            actual.setNombre(tipoCategoria.getNombre());
            actual.setDescripcion(tipoCategoria.getDescripcion());
            actual.setImagen(tipoCategoria.getImagen());
            return ResponseEntity.ok(tipoCategoriaRepository.save(actual));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (tipoCategoriaRepository.existsById(id)) {
            tipoCategoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
