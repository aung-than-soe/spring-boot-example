package org.example.sharedschema.dto;

import java.math.BigDecimal;

public record ProductDTO(Long categoryId, String name, String code, String description, BigDecimal price) {
}
