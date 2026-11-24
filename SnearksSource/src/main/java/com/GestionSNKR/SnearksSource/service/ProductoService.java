package com.GestionSNKR.SnearksSource.service;

import com.GestionSNKR.SnearksSource.model.Producto;
import com.GestionSNKR.SnearksSource.model.TipoCategoria;
import com.GestionSNKR.SnearksSource.repository.ProductoRepository;
import com.GestionSNKR.SnearksSource.repository.TipoCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final TipoCategoriaRepository tipoCategoriaRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, TipoCategoriaRepository tipoCategoriaRepository) {
        this.productoRepository = productoRepository;
        this.tipoCategoriaRepository = tipoCategoriaRepository;
    }

    // Obtener todos los productos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Obtener productos filtrados por tipo de categoría
    public List<Producto> obtenerPorTipoCategoria(String tipoCategoria) {
        return productoRepository.findByTipoCategoriaNombreIgnoreCase(tipoCategoria);
    }

    // Obtener un producto por ID
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    private TipoCategoria resolverTipoCategoria(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return null;
        }

        String nombreNormalizado = nombre.trim();
        return tipoCategoriaRepository.findByNombreIgnoreCase(nombreNormalizado)
                .orElseGet(() -> tipoCategoriaRepository.save(new TipoCategoria(nombreNormalizado)));
    }

    // Guardar un nuevo producto
    public Producto guardarProducto(Producto producto) {
        if (producto.getTipoCategoria() == null && producto.getTipoCategoriaNombre() != null) {
            producto.setTipoCategoria(resolverTipoCategoria(producto.getTipoCategoriaNombre()));
        }
        return productoRepository.save(producto);
    }

    // Actualizar un producto existente
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombreModelo(productoDetalles.getNombreModelo());
            producto.setPrecio(productoDetalles.getPrecio());
            producto.setDescripcion(productoDetalles.getDescripcion());
            producto.setLinkImagen(productoDetalles.getLinkImagen());
            if (productoDetalles.getTipoCategoria() != null) {
                producto.setTipoCategoria(resolverTipoCategoria(productoDetalles.getTipoCategoria().getNombre()));
            } else if (productoDetalles.getTipoCategoriaNombre() != null) {
                producto.setTipoCategoria(resolverTipoCategoria(productoDetalles.getTipoCategoriaNombre()));
            }
            producto.setTallasDisponibles(productoDetalles.getTallasDisponibles());
            return productoRepository.save(producto);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    // Eliminar un producto
    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}