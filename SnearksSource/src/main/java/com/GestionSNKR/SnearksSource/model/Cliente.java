package com.GestionSNKR.SnearksSource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un Cliente en la base de datos.
 */
@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombreCompleto", length = 150, nullable = false)
    @JsonProperty("nombreCompleto")
    private String nombreCompleto;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "google_sub", length = 255, unique = true)
    private String googleSub;

    @Column(name = "rol", length = 20, nullable = false)
    private String rol = "CLIENTE";

    @Column(name = "contrasena", length = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    @Column(name = "numero", length = 9)
    private String numero;
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "region", length = 100)
    private String region;


    @Column(name = "comuna", length = 100)
    private String comuna;



    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Carrito carrito;
}
