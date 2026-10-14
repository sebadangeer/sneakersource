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
@Table(name = "tipo_categoria")
@Data
@NoArgsConstructor
public class TipoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion = "";

    @Column(name = "imagen", nullable = false)
    private String imagen = "";

    public TipoCategoria(String nombre) {
        this.nombre = nombre;
        this.descripcion = "Categoría " + nombre;
        this.imagen = "";
    }

    public TipoCategoria(String nombre, String descripcion, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion != null ? descripcion : "";
        this.imagen = imagen != null ? imagen : "";
    }
}
