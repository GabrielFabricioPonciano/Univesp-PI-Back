package com.univesp.projeto_integrador.service;

import com.univesp.projeto_integrador.dto.ProductDTO;
import com.univesp.projeto_integrador.dto.PromotionDTO;
import com.univesp.projeto_integrador.exception.ResourceNotFoundException;
import com.univesp.projeto_integrador.model.Product;
import com.univesp.projeto_integrador.model.Promotion;
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

    @Autowired
    private PromotionService promotionService;


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
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id " + id));

        // Atualiza os valores do produto
        existingProduct.setProductName(newProductDetails.getProductName());
        existingProduct.setProductType(newProductDetails.getProductType());
        existingProduct.setQuantity(newProductDetails.getQuantity());
        existingProduct.setNumberLote(newProductDetails.getNumberLote());
        existingProduct.setDescription(newProductDetails.getDescription());
        existingProduct.setDateExpiration(newProductDetails.getDateExpiration());

        // Atualiza os valores de preços
        validateBigDecimalField(newProductDetails.getPriceForLote(), "O preço por lote");
        validateBigDecimalField(newProductDetails.getGainPercentage(), "A porcentagem de ganho");
        existingProduct.setPriceForLote(newProductDetails.getPriceForLote());
        existingProduct.setGainPercentage(newProductDetails.getGainPercentage());

        BigDecimal priceForLotePercent = calculatePriceForLotePercent(
                existingProduct.getPriceForLote(), existingProduct.getGainPercentage()
        );
        existingProduct.setPriceForLotePercent(priceForLotePercent);
        BigDecimal priceForUnity = calculateUnitPrice(existingProduct.getPriceForLote(), existingProduct.getQuantity());
        existingProduct.setPriceForUnity(priceForUnity);
        BigDecimal priceForUnityPercent = calculateUnitPrice(existingProduct.getPriceForLotePercent(), existingProduct.getQuantity());
        existingProduct.setPriceForUnityPercent(priceForUnityPercent);

        // Verifica se é para remover a promoção
        if (newProductDetails.getPromotion() == null) {
            existingProduct.setPromotion(null);  // Remove a promoção se for null
        } else {
            // Atualiza a promoção associada, se houver
            PromotionDTO promotionDTO = promotionService.getPromotionById(newProductDetails.getPromotion().getPromotionId());
            Promotion promotion = promotionService.dtoToEntity(promotionDTO);
            existingProduct.setPromotion(promotion);
        }

        Product savedProduct = productRepository.save(existingProduct);
        return entityToDto(savedProduct);
    }



    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);

        // Verifica se a quantidade é válida
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        // Valida campos numéricos
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

        // Associa a promoção ao produto
        if (productDTO.getPromotion() != null) {
            PromotionDTO promotionDTO = promotionService.getPromotionById(productDTO.getPromotion().getPromotionId());
            Promotion promotion = promotionService.dtoToEntity(promotionDTO); // Converte DTO para entidade
            product.setPromotion(promotion);
        }

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



    private Product dtoToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setProductType(productDTO.getProductType());
        product.setQuantity(productDTO.getQuantity());
        product.setNumberLote(productDTO.getNumberLote());
        product.setDateExpiration(productDTO.getDateExpiration());
        product.setGainPercentage(productDTO.getGainPercentage());
        product.setPriceForLote(productDTO.getPriceForLote());
        product.setDescription(productDTO.getDescription());

        // Adiciona promoção associada, se existir
        if (productDTO.getPromotion() != null) {
            Promotion promotion = promotionService.dtoToEntity(productDTO.getPromotion());
            product.setPromotion(promotion);
        }

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

        // Adiciona promoção associada, se houver
        if (product.getPromotion() != null) {
            PromotionDTO promotionDTO = promotionService.entityToDto(product.getPromotion());
            dto.setPromotion(promotionDTO);
        }

        return dto;
    }







}
