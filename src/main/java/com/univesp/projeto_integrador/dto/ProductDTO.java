package com.univesp.projeto_integrador.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productType;
    private int quantity;
    private String numberLote;
    private String description;
    private LocalDate dateExpiration;
    private BigDecimal gainPercentage;       // Campo para a porcentagem de ganho
    private BigDecimal priceForLotePercent;  // Campo para o preço total com ganho
    private BigDecimal priceForLote;         // Preço calculado automaticamente (base)
    private BigDecimal priceForUnityPercent; // Preço unitário com ganho
    private BigDecimal priceForUnity;        // Preço unitário base
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Productstatus status;            // Status do produto (ACTIVE, SOLD, ROTTEN)

    public enum Productstatus {
        ACTIVE, SOLD, ROTTEN
    }
}
