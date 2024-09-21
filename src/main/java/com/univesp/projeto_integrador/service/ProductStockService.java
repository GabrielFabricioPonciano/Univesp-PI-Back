package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.ProductStock;
import com.univesp.projeto_integrador.repository.ProductStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    @Autowired
    public ProductStockService(ProductStockRepository productStockRepository) {
        this.productStockRepository = productStockRepository;
    }

    // Método para listar todos os produtos no estoque
    public List<ProductStock> getAllStock() {
        return productStockRepository.findAll();
    }

    // Método para buscar um produto no estoque pelo ID
    public Optional<ProductStock> getStockById(Long id) {
        return productStockRepository.findById(id);
    }

    // Método para adicionar um novo estoque de produto
    public ProductStock addStock(ProductStock productStock) {
        return productStockRepository.save(productStock);
    }

    // Método para atualizar um estoque de produto
    public ProductStock updateStock(Long id, ProductStock updatedStock) {
        Optional<ProductStock> existingStock = productStockRepository.findById(id);
        if (existingStock.isPresent()) {
            ProductStock stock = existingStock.get();
            stock.setQuantity(updatedStock.getQuantity());
            stock.setPricePerUnit(updatedStock.getPricePerUnit());
            stock.setExpirationDate(updatedStock.getExpirationDate());
            return productStockRepository.save(stock);
        } else {
            throw new RuntimeException("Stock not found with id: " + id);
        }
    }

    // Método para deletar um estoque de produto
    public void deleteStock(Long id) {
        productStockRepository.deleteById(id);
    }

    public List<ProductStock> findAll() {
        return productStockRepository.findAll();
    }
    public ProductStock findById(long id){
        return productStockRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));
    }


}
