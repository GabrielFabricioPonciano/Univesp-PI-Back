package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.Product;
import com.univesp.projeto_integrador.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));
    }

    public Product saveProduct(Product product) {
        System.out.println("salvado" + product);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product newproductdetails) {
        Product productmod = findById(id);
        productmod.setProductName(newproductdetails.getProductName());
        productmod.setProductType(newproductdetails.getProductType());
        productmod.setDescription(newproductdetails.getDescription());
        return productRepository.save(productmod);
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)){
            Optional<Product> produto = productRepository.findById(id);
            produto.ifPresent(product -> System.out.println("Excluido produto: " + product.getProductName()));
        }
        productRepository.deleteById(id);
    }
}
