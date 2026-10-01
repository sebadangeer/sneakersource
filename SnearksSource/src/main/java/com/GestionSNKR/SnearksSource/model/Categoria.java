package com.GestionSNKR.SnearksSource.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true)
    private String nombre;

    @Column(name = "descripcion_categoria")
    private String descripcionCategoria;

    @Column(name = "imagen_categoria")
    private String imagenCategoria;
}