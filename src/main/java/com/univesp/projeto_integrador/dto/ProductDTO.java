package com.univesp.projeto_integrador.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private int quantity;
    private String numberlote;
    private String type;
    private LocalDateTime expiryDate; // Ajustado para corresponder a LocalDateTime
    private BigDecimal unitPrice; // Ajustado para BigDecimal, que representa valores monetários
    private BigDecimal priceforlote; // Ajustado para BigDecimal
    private String description;
}
