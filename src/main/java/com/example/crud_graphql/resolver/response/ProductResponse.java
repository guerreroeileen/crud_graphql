package com.example.crud_graphql.resolver.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id, String name, String description, BigDecimal price, Integer stock,
                              CategoryResponse categoryResponse) {
}
