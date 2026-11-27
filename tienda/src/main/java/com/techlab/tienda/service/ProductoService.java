package com.techlab.tienda.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techlab.tienda.dto.ProductoConCategoria;
import com.techlab.tienda.exception.ProductoDuplicadoException;
import com.techlab.tienda.exception.ProductoNoEncontradoException;
import com.techlab.tienda.model.Categoria;
import com.techlab.tienda.model.Producto;
import com.techlab.tienda.repository.CategoriaRepository;
import com.techlab.tienda.repository.ProductoRepository;
import com.techlab.tienda.util.Validador;


@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public Producto obtenerPorId(int id) {
        Optional<Producto> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ProductoNoEncontradoException("No se encontro un producto con el id " + id);
        }
        return resultado.get();
    }

    public Producto guardar(Producto p) {
        Validador.validarNombre(p.getNombre());
        Validador.validarPrecio(p.getPrecio());
        Validador.validarStock(p.getStock());

        if (repository.existsByNombreIgnoreCase(p.getNombre())) {
            throw new ProductoDuplicadoException("Ya existe un producto con el nombre " + p.getNombre());
        }
        p.setId(0);
        p.setCategoria(obtenerCategoria(p));
        return repository.save(p);
    }

    public Producto actualizar(int id, Producto datos) {
        Producto p = obtenerPorId(id);

        Validador.validarNombre(datos.getNombre());
        Validador.validarPrecio(datos.getPrecio());
        Validador.validarStock(datos.getStock());

        if (repository.existsByNombreIgnoreCaseAndIdNot(datos.getNombre(), id)) {
            throw new ProductoDuplicadoException("Ya existe un producto con el nombre " + datos.getNombre());
        }

        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setCategoria(obtenerCategoria(datos));

        return repository.save(p);
    }

    public void eliminar(int id) {
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return repository.findByCategoriaNombre(categoria);
    }

    private Categoria obtenerCategoria(Producto p) {
        if (p.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }
        int idCategoria = p.getCategoria().getId();
        Optional<Categoria> resultado = categoriaRepository.findById(idCategoria);
        if (resultado.isEmpty()) {
            throw new IllegalArgumentException("No existe la categoría con id " + idCategoria);
        }
        return resultado.get();
    }

    public List<Producto> buscarPorNombre(String texto) {
        return repository.findByNombreContainingIgnoreCase(texto);
    }

    public BigDecimal calcularValorInventario() {
        BigDecimal valor = repository.calcularValorInventario();
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return valor;
    }

    public List<ProductoConCategoria> listarConCategoria() {
        return repository.listarConCategoria();
    }

    public void descontarStock(Producto p, int cantidad) {
        p.setStock(p.getStock() - cantidad);
        repository.save(p);
    }
}