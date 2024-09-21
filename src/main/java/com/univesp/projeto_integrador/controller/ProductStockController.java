package com.univesp.projeto_integrador.controller;

import com.univesp.projeto_integrador.model.ProductStock;
import com.univesp.projeto_integrador.service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-stock")
public class ProductStockController {

    @Autowired
    private ProductStockService productStockService;

    // Buscar todo o estoque
    @GetMapping
    public List<ProductStock> getAllStock() {
        return productStockService.findAll();
    }

    // Buscar estoque por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductStock> getStockById(@PathVariable Long id) {
        ProductStock stock = productStockService.findById(id);
        return ResponseEntity.ok(stock);
    }

    // Criar novo registro de estoque
    @PostMapping
    public ResponseEntity<ProductStock> createStock(@RequestBody ProductStock stock) {
        ProductStock createdStock = productStockService.addStock(stock);
        return ResponseEntity.ok(createdStock);
    }

    // Atualizar estoque existente
    @PutMapping("/{id}")
    public ResponseEntity<ProductStock> updateStock(@PathVariable Long id, @RequestBody ProductStock stockDetails) {
        ProductStock updatedStock = productStockService.updateStock(id, stockDetails);
        return ResponseEntity.ok(updatedStock);
    }

    // Deletar registro de estoque
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long id) {
        productStockService.deleteStock(id);
        return ResponseEntity.ok().build();
    }
}
