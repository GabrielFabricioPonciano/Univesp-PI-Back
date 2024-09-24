package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.dto.ProductDTO;
import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.Product;
import com.univesp.projeto_integrador.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> findAll() {
        List<Product> produtos = productRepository.findAll();
        return produtos.stream().map(this::entityToDto).collect(Collectors.toList());

    };

    public ProductDTO findById(Long id) {
        Optional<Product> produto = productRepository.findById(id);
        if (produto.isPresent()){
            ProductDTO produtodto = entityToDto(produto.get());
            return produtodto;
        }else {
            throw new ResourceNotFoundException("Produto não encontrado com id " + id);
    }}

    public ProductDTO saveProduct(ProductDTO product) {
        Product produto = dtoToEntity(product);
          Product salvoproduto = productRepository.save(produto);
          return entityToDto(salvoproduto);
    }

    public ProductDTO updateProduct(Long id, ProductDTO newProductDetails) {
        // Encontra o produto existente pelo ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));

        // Atualiza os valores do produto existente com base no DTO fornecido
        existingProduct.setProductName(newProductDetails.getName());
        existingProduct.setProductType(newProductDetails.getType());
        existingProduct.setQuantity(newProductDetails.getQuantity());
        existingProduct.setNumberlote(newProductDetails.getNumberlote());
        existingProduct.setDescription(newProductDetails.getDescription());
        existingProduct.setDateexpiration(newProductDetails.getExpiryDate());
        existingProduct.setPriceforunity(newProductDetails.getUnitPrice());
        existingProduct.setPriceforlote(newProductDetails.getPriceforlote());

        // Salva o produto atualizado no banco de dados
        productRepository.save(existingProduct);

        // Converte o produto atualizado de volta para DTO e retorna
        return entityToDto(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)){
            Optional<Product> produto = productRepository.findById(id);
            produto.ifPresent(product -> System.out.println("Excluido produto: " + product.getProductName()));
        }
        productRepository.deleteById(id);
    }

    private Product dtoToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductName(dto.getName());
        product.setProductType(dto.getType());
        product.setQuantity(dto.getQuantity());
        product.setNumberlote(dto.getNumberlote());
        product.setDateexpiration(dto.getExpiryDate());
        product.setPriceforunity(dto.getUnitPrice());
        product.setPriceforlote(dto.getPriceforlote());
        product.setDescription(dto.getDescription());
        return product;
    }

    // Converter Entidade para DTO
    private ProductDTO entityToDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getProductId());
        dto.setName(product.getProductName());
        dto.setType(product.getProductType());
        dto.setQuantity(product.getQuantity());
        dto.setNumberlote(product.getNumberlote());
        dto.setExpiryDate(product.getDateexpiration());
        dto.setUnitPrice(product.getPriceforunity());
        dto.setPriceforlote(product.getPriceforlote());
        dto.setDescription(product.getDescription());
        return dto;
    }

}
