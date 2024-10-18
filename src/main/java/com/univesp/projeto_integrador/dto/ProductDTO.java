package com.univesp.projeto_integrador.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 100, message = "O nome do produto pode ter no máximo 100 caracteres.")
    private String productName;

    @Size(max = 50, message = "O tipo de produto pode ter no máximo 50 caracteres.")
    private String productType;

    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private int quantity;

    @NotBlank(message = "O número do lote é obrigatório.")
    @Size(max = 50, message = "O número do lote pode ter no máximo 50 caracteres.")
    private String numberLote;

    private String description;

    @NotNull(message = "A data de validade é obrigatória.")
    @Future(message = "A data de validade deve ser uma data futura.")
    private LocalDate dateExpiration;

    @NotNull(message = "A porcentagem de ganho é obrigatória.")
    @DecimalMin(value = "0.00", message = "A porcentagem de ganho deve ser no mínimo 0%.")
    @Digits(integer = 3, fraction = 2, message = "A porcentagem de ganho deve ter no máximo 3 dígitos inteiros e 2 decimais.")
    private BigDecimal gainPercentage;

    @NotNull(message = "O preço por lote com percentual é obrigatório.")
    @DecimalMin(value = "0.00", message = "O preço por lote com percentual deve ser no mínimo 0.")
    @Digits(integer = 10, fraction = 2, message = "O preço por lote com percentual deve ter no máximo 10 dígitos inteiros e 2 decimais.")
    private BigDecimal priceForLotePercent;

    @NotNull(message = "O preço por lote é obrigatório.")
    @DecimalMin(value = "0.00", message = "O preço por lote deve ser no mínimo 0.")
    @Digits(integer = 10, fraction = 2, message = "O preço por lote deve ter no máximo 10 dígitos inteiros e 2 decimais.")
    private BigDecimal priceForLote;

    @DecimalMin(value = "0.00", message = "O preço unitário com percentual deve ser no mínimo 0.")
    @Digits(integer = 10, fraction = 2, message = "O preço unitário com percentual deve ter no máximo 10 dígitos inteiros e 2 decimais.")
    private BigDecimal priceForUnityPercent;

    @DecimalMin(value = "0.00", message = "O preço unitário deve ser no mínimo 0.")
    @Digits(integer = 10, fraction = 2, message = "O preço unitário deve ter no máximo 10 dígitos inteiros e 2 decimais.")
    private BigDecimal priceForUnity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "O status do produto é obrigatório.")
    private String status;

    private PromotionDTO promotion;

    // Construtor sem argumentos (necessário para deserialização)
    public ProductDTO() {}

    // Construtor opcional com argumentos
    public ProductDTO(Long productId, String productName, String productType, int quantity,
                      String numberLote, String description, LocalDate dateExpiration,
                      BigDecimal gainPercentage, BigDecimal priceForLotePercent, BigDecimal priceForLote,
                      BigDecimal priceForUnityPercent, BigDecimal priceForUnity, LocalDateTime createdAt,
                      LocalDateTime updatedAt, String status, PromotionDTO promotion) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.quantity = quantity;
        this.numberLote = numberLote;
        this.description = description;
        this.dateExpiration = dateExpiration;
        this.gainPercentage = gainPercentage;
        this.priceForLotePercent = priceForLotePercent;
        this.priceForLote = priceForLote;
        this.priceForUnityPercent = priceForUnityPercent;
        this.priceForUnity = priceForUnity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.promotion = promotion;
    }
}
