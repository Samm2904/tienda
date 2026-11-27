package com.techlab.tienda.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.tienda.dto.ProductoConCategoria;
import com.techlab.tienda.model.Producto;
import com.techlab.tienda.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping 
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria)); 
    }
    
    @GetMapping ("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping 
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevo = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable int id, @RequestBody Producto datos) {
        return ResponseEntity.ok(productoService.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

      @GetMapping(params = "nombre")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @GetMapping("/valor-inventario")
    public ResponseEntity<BigDecimal> valorInventario() {
        return ResponseEntity.ok(productoService.calcularValorInventario());
    }

    @GetMapping("/con-categoria")
    public ResponseEntity<List<ProductoConCategoria>> listarConCategoria() {
        return ResponseEntity.ok(productoService.listarConCategoria());
    }
}