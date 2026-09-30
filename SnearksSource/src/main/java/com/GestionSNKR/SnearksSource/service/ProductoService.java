package com.GestionSNKR.SnearksSource.service;

import com.GestionSNKR.SnearksSource.model.Producto;
import com.GestionSNKR.SnearksSource.model.Categoria;
import com.GestionSNKR.SnearksSource.repository.ProductoRepository;
import com.GestionSNKR.SnearksSource.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
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

    // Guardar un nuevo producto
    public Producto guardarProducto(Producto producto) {
        if (producto.getCategoria() == null && producto.getTipoCategoria() != null) {
            String nombre = producto.getTipoCategoria().trim();
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
            // Manejar categoria por nombre o por objeto
            if (productoDetalles.getCategoria() != null) {
                producto.setCategoria(productoDetalles.getCategoria());
            } else if (productoDetalles.getTipoCategoria() != null) {
                String nombre = productoDetalles.getTipoCategoria().trim();
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