package com.univesp.projeto_integrador.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

@Getter
public enum ProductStatus {
    ACTIVE,
    INACTIVE
}

