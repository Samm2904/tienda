package com.techlab.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.tienda.model.LineaPedido;

public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Integer> {

    List<LineaPedido> findByPedidoId(int pedidoId);
    boolean existsByProductoId(int productoId);
}