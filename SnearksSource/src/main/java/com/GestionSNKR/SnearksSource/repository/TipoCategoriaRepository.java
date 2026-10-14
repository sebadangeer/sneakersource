package com.GestionSNKR.SnearksSource.repository;

import com.GestionSNKR.SnearksSource.model.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoCategoriaRepository extends JpaRepository<TipoCategoria, Long> {
    Optional<TipoCategoria> findByNombreIgnoreCase(String nombre);
}
