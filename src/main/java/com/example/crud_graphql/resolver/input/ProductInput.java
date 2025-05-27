package com.example.crud_graphql.resolver.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductInput {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
    private UUID categoryId;
}
