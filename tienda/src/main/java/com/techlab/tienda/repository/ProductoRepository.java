package com.techlab.tienda.repository;

import com.techlab.tienda.dto.ProductoConCategoria;
import com.techlab.tienda.model.Producto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreContainingIgnoreCase(String texto);
    List<Producto> findByCategoriaNombre(String nombre);

    @Query("SELECT SUM(p.precio * p.stock) FROM Producto p")
    BigDecimal calcularValorInventario();

    @Query("SELECT new com.techlab.tienda.dto.ProductoConCategoria(p.id, p.nombre, p.precio, p.stock, c.nombre) "
            + "FROM Producto p JOIN p.categoria c")
    List<ProductoConCategoria> listarConCategoria();

    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, int id);
}