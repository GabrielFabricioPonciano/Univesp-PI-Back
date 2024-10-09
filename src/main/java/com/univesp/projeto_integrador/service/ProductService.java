package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.dto.ProductDTO;
import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.Product;
import com.univesp.projeto_integrador.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    // Método para verificar se um valor BigDecimal é nulo ou inválido (<= 0)




    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            System.out.println("Excluindo produto: " + product.get().getProductName());
            productRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Produto não encontrado com o ID: " + id);
        }
    }

    // Método para verificar se um valor BigDecimal é nulo ou inválido (<= 0)
    private void validateBigDecimalField(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " não pode ser nulo ou menor/igual a zero.");
        }
    }

    public ProductDTO updateProduct(Long id, ProductDTO newProductDetails) {
        // Encontra o produto existente pelo ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));

        // Atualiza os valores do produto existente com base no DTO fornecido
        existingProduct.setProductName(newProductDetails.getProductName());
        existingProduct.setProductType(newProductDetails.getProductType());
        existingProduct.setQuantity(newProductDetails.getQuantity());
        existingProduct.setNumberLote(newProductDetails.getNumberLote());
        existingProduct.setDescription(newProductDetails.getDescription());
        existingProduct.setDateExpiration(newProductDetails.getDateExpiration());

        // Valida apenas priceForLote e gainPercentage, pois priceForLotePercent será calculado
        validateBigDecimalField(newProductDetails.getPriceForLote(), "O preço por lote");
        validateBigDecimalField(newProductDetails.getGainPercentage(), "A porcentagem de ganho");

        // Atualiza priceForLote e gainPercentage
        existingProduct.setPriceForLote(newProductDetails.getPriceForLote());
        existingProduct.setGainPercentage(newProductDetails.getGainPercentage());

        // Calcula o preço final do lote com a margem de ganho
        BigDecimal priceForLotePercent = calculatePriceForLotePercent(
                existingProduct.getPriceForLote(), existingProduct.getGainPercentage()
        );
        existingProduct.setPriceForLotePercent(priceForLotePercent);

        // Calcula os preços unitários
        BigDecimal priceForUnity = calculateUnitPrice(existingProduct.getPriceForLote(), existingProduct.getQuantity());
        existingProduct.setPriceForUnity(priceForUnity);

        // Calcula o preço unitário com a margem de ganho
        BigDecimal priceForUnityPercent = calculateUnitPrice(existingProduct.getPriceForLotePercent(), existingProduct.getQuantity());
        existingProduct.setPriceForUnityPercent(priceForUnityPercent);

        // Salva o produto atualizado no banco de dados
        Product savedProduct = productRepository.save(existingProduct);

        // Converte o produto atualizado de volta para DTO e retorna
        return entityToDto(savedProduct);
    }


    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);

        // Verifica se a quantidade é válida
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }


        // Valida apenas priceForLote e gainPercentage, pois priceForLotePercent será calculado
        validateBigDecimalField(product.getPriceForLote(), "O preço por lote");
        validateBigDecimalField(product.getGainPercentage(), "A porcentagem de ganho");

        // Calcula o preço final do lote com a margem de ganho
        BigDecimal priceForLotePercent = calculatePriceForLotePercent(product.getPriceForLote(), product.getGainPercentage());
        product.setPriceForLotePercent(priceForLotePercent);

        // Calcula o preço unitário com e sem margem
        BigDecimal priceForUnity = calculateUnitPrice(product.getPriceForLote(), product.getQuantity());
        BigDecimal priceForUnityPercent = calculateUnitPrice(priceForLotePercent, product.getQuantity());
        product.setPriceForUnity(priceForUnity);
        product.setPriceForUnityPercent(priceForUnityPercent);

        // Salva no banco de dados
        Product savedProduct = productRepository.save(product);

        return entityToDto(savedProduct);
    }

    // Método para calcular o preço do lote a partir de priceForLote e gainPercentage
    private BigDecimal calculatePriceForLotePercent(BigDecimal priceForLote, BigDecimal gainPercentage) {
        BigDecimal gainFactor = BigDecimal.ONE.add(gainPercentage.divide(new BigDecimal("100"), RoundingMode.HALF_UP));
        return priceForLote.multiply(gainFactor).setScale(2, RoundingMode.HALF_UP);
    }

    // Método para calcular o preço unitário
    private BigDecimal calculateUnitPrice(BigDecimal priceForLote, int quantity) {
        if (quantity > 0) {
            return priceForLote.divide(new BigDecimal(quantity), RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }



    private Product dtoToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setProductType(dto.getProductType());
        product.setQuantity(dto.getQuantity());
        product.setNumberLote(dto.getNumberLote());
        product.setDateExpiration(dto.getDateExpiration());
        product.setGainPercentage(dto.getGainPercentage());
        product.setPriceForLote(dto.getPriceForLote());
        product.setDescription(dto.getDescription());

        // Estes campos serão calculados no backend e não devem ser enviados pelo cliente diretamente.
        // Certifique-se de que estes campos sejam inicializados corretamente na lógica de negócio.
        product.setPriceForUnity(dto.getPriceForUnity());
        product.setPriceForUnityPercent(dto.getPriceForUnityPercent());
        product.setPriceForLotePercent(dto.getPriceForLotePercent());

        return product;
    }

    private ProductDTO entityToDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductType(product.getProductType());
        dto.setQuantity(product.getQuantity());
        dto.setNumberLote(product.getNumberLote());
        dto.setDateExpiration(product.getDateExpiration());
        dto.setGainPercentage(product.getGainPercentage());
        dto.setPriceForLote(product.getPriceForLote());
        dto.setDescription(product.getDescription());

        // Valores calculados que são enviados ao frontend para exibição
        dto.setPriceForUnity(product.getPriceForUnity());
        dto.setPriceForUnityPercent(product.getPriceForUnityPercent());
        dto.setPriceForLotePercent(product.getPriceForLotePercent());

        return dto;
    }






}
