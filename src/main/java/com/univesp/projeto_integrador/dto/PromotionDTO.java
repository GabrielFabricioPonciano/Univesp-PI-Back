package com.univesp.projeto_integrador.dto;

import com.univesp.projeto_integrador.model.PromotionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
public class PromotionDTO {
    private Long promotionId;
    private String promotionDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal discountPercentage;
    private PromotionStatus status;
}

