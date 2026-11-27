package com.techlab.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.tienda.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}