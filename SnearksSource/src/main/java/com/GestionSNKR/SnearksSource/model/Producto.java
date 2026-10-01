package com.GestionSNKR.SnearksSource.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "nombre_modelo")
    private String nombreModelo;

    @Column(name = "descripcion", length = 600)
    private String descripcion;

    @Column(name = "link_imagen")
    private String linkImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_categoria_id")
    private TipoCategoria tipoCategoria;

    @Transient
    private String tipoCategoriaNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ElementCollection
    @CollectionTable(
            name = "producto_tallas",
            joinColumns = @JoinColumn(name = "producto_id")
    )
    @MapKeyColumn(name = "talla")
    @Column(name = "cantidad")
    private Map<String, Integer> tallasDisponibles = new HashMap<>();

    @JsonSetter("tipoCategoria")
    public void setTipoCategoriaFromJson(Object value) {
        if (value == null) {
            this.tipoCategoria = null;
            this.tipoCategoriaNombre = null;
            return;
        }

        if (value instanceof TipoCategoria tipo) {
            this.tipoCategoria = tipo;
            this.tipoCategoriaNombre = tipo.getNombre();
            return;
        }

        if (value instanceof String nombre) {
            this.tipoCategoriaNombre = nombre.trim();
            this.tipoCategoria = null;
            return;
        }

        if (value instanceof Map<?, ?> map) {
            Object nombre = map.get("nombre");
            if (nombre != null) {
                this.tipoCategoriaNombre = nombre.toString().trim();
                this.tipoCategoria = null;
            }
        }
    }

    @JsonGetter("tipoCategoria")
    public Object getTipoCategoriaJson() {
        if (tipoCategoria != null) {
            return tipoCategoria;
        }
        return tipoCategoriaNombre;
    }

    public void setTipoCategoria(TipoCategoria tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
        this.tipoCategoriaNombre = tipoCategoria != null ? tipoCategoria.getNombre() : null;
    }

    public String getTipoCategoriaNombre() {
        return tipoCategoria != null ? tipoCategoria.getNombre() : tipoCategoriaNombre;
    }

    public void setTipoCategoriaNombre(String tipoCategoriaNombre) {
        this.tipoCategoriaNombre = tipoCategoriaNombre;
    }

    public Producto(Long id, Integer precio, String nombreModelo, String descripcion, String linkImagen, String tipoCategoria) {
        this.id = id;
        this.precio = precio;
        this.nombreModelo = nombreModelo;
        this.descripcion = descripcion;
        this.linkImagen = linkImagen;
        this.tipoCategoriaNombre = tipoCategoria;
        this.tallasDisponibles = new HashMap<>();
    }

    public Producto(Long id, Integer precio, String nombreModelo, String descripcion, String linkImagen, TipoCategoria tipoCategoria) {
        this.id = id;
        this.precio = precio;
        this.nombreModelo = nombreModelo;
        this.descripcion = descripcion;
        this.linkImagen = linkImagen;
        this.tipoCategoria = tipoCategoria;
        this.tipoCategoriaNombre = tipoCategoria != null ? tipoCategoria.getNombre() : null;
        this.tallasDisponibles = new HashMap<>();
    }

    public Producto(Long id, Integer precio, String nombreModelo, String descripcion, String linkImagen, String tipoCategoria, Map<String, Integer> tallasDisponibles) {
        this.id = id;
        this.precio = precio;
        this.nombreModelo = nombreModelo;
        this.descripcion = descripcion;
        this.linkImagen = linkImagen;
        this.tipoCategoriaNombre = tipoCategoria;
        this.tallasDisponibles = tallasDisponibles == null ? new HashMap<>() : new HashMap<>(tallasDisponibles);
    }

    public Producto(Long id, Integer precio, String nombreModelo, String descripcion, String linkImagen, TipoCategoria tipoCategoria, Map<String, Integer> tallasDisponibles) {
        this.id = id;
        this.precio = precio;
        this.nombreModelo = nombreModelo;
        this.descripcion = descripcion;
        this.linkImagen = linkImagen;
        this.tipoCategoria = tipoCategoria;
        this.tipoCategoriaNombre = tipoCategoria != null ? tipoCategoria.getNombre() : null;
        this.tallasDisponibles = tallasDisponibles == null ? new HashMap<>() : new HashMap<>(tallasDisponibles);
    }

}