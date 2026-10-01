package com.GestionSNKR.SnearksSource.service;

import com.GestionSNKR.SnearksSource.model.Categoria;
import com.GestionSNKR.SnearksSource.model.Producto;
import com.GestionSNKR.SnearksSource.model.TipoCategoria;
import com.GestionSNKR.SnearksSource.repository.CategoriaRepository;
import com.GestionSNKR.SnearksSource.repository.ProductoRepository;
import com.GestionSNKR.SnearksSource.repository.TipoCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoCategoriaRepository tipoCategoriaRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, TipoCategoriaRepository tipoCategoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoCategoriaRepository = tipoCategoriaRepository;
    }

    // Obtener todos los productos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Obtener productos filtrados por categoría (por nombre de categoria)
    public List<Producto> obtenerPorCategoria(String categoria) {
        return productoRepository.findByCategoriaNombreIgnoreCase(categoria);
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

        if (producto.getCategoria() == null && producto.getTipoCategoria() != null) {
            String nombre = producto.getTipoCategoria().getNombre();
            Categoria cat = categoriaRepository.findByNombreIgnoreCase(nombre)
                    .orElseGet(() -> {
                        Categoria nueva = new Categoria();
                        nueva.setNombre(nombre);
                        return categoriaRepository.save(nueva);
                    });
            producto.setCategoria(cat);
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
            if (productoDetalles.getCategoria() != null) {
                producto.setCategoria(productoDetalles.getCategoria());
            } else if (productoDetalles.getTipoCategoria() != null) {
                producto.setTipoCategoria(productoDetalles.getTipoCategoria());
                String nombre = productoDetalles.getTipoCategoria().getNombre();
                Categoria cat = categoriaRepository.findByNombreIgnoreCase(nombre)
                        .orElseGet(() -> {
                            Categoria nueva = new Categoria();
                            nueva.setNombre(nombre);
                            return categoriaRepository.save(nueva);
                        });
                producto.setCategoria(cat);
            } else if (productoDetalles.getTipoCategoriaNombre() != null) {
                producto.setTipoCategoria(resolverTipoCategoria(productoDetalles.getTipoCategoriaNombre()));
                String nombre = producto.getTipoCategoria().getNombre();
                Categoria cat = categoriaRepository.findByNombreIgnoreCase(nombre)
                        .orElseGet(() -> {
                            Categoria nueva = new Categoria();
                            nueva.setNombre(nombre);
                            return categoriaRepository.save(nueva);
                        });
                producto.setCategoria(cat);
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