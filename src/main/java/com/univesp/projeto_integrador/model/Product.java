package com.univesp.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 50)
    private String productType;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 50, nullable = false)
    private String numberLote;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDate dateExpiration;

    @Column(nullable = false)
    private BigDecimal gainPercentage;  // Novo campo para a porcentagem de ganho

    @Column(nullable = false)
    private BigDecimal priceForLotePercent;  // Novo campo para o preço total


    @Column(nullable = false)
    private BigDecimal priceForLote; // Preço calculado automaticamente

    @Column
    private BigDecimal priceForUnityPercent; // Preço calculado automaticamente

    @Column
    private BigDecimal priceForUnity;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    Productstatus status = Productstatus.ACTIVE;

    enum Productstatus {
        ACTIVE, SOLD, ROTTEN
    }


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.priceForLotePercent = calculatePriceForLotePercent(); // Calcula o preço do lote com a margem de lucro
        this.priceForUnityPercent = calculateUnitPricePercent();   // Calcula o preço unitário com base no preço do lote com margem
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.priceForLotePercent = calculatePriceForLotePercent(); // Calcula o preço do lote com a margem de lucro
        this.priceForUnityPercent = calculateUnitPricePercent();   // Calcula o preço unitário com base no preço do lote com margem
    }


    // Lógica para calcular o preço por lote com base no totalPrice e gainPercentage
    private BigDecimal calculatePriceForLotePercent() {
        if (priceForLote != null && gainPercentage != null) {
            BigDecimal gainFactor = BigDecimal.ONE.add(gainPercentage.divide(new BigDecimal("100")));
            return priceForLote.multiply(gainFactor).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO; // Valor padrão se os dados forem inválidos
    }



    // Lógica para calcular o preço unitário com base no preço por lote e quantidade
    private BigDecimal calculateUnitPricePercent() {
        if (quantity > 0 && priceForLotePercent != null) {
            return priceForLotePercent.divide(new BigDecimal(quantity), RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO; // Valor padrão se os dados forem inválidos
    }

}
