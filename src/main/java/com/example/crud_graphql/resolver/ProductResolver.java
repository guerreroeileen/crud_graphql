package com.example.crud_graphql.resolver;

import com.example.crud_graphql.resolver.input.ProductInput;
import com.example.crud_graphql.resolver.response.ProductPageResponse;
import com.example.crud_graphql.resolver.response.ProductResponse;
import com.example.crud_graphql.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductResolver {

    private final ProductService productService;

    @QueryMapping
    public ProductPageResponse products(@Argument String productName, @Argument UUID categoryId, @Argument String sortDirection, @Argument Integer page, @Argument Integer size) {
        var result = productService.getAllProducts(page, size, productName, categoryId, sortDirection);
        return new ProductPageResponse(result.getContent(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize(),
                result.isFirst(),
                result.isLast());
    }

    @QueryMapping
    public ProductResponse productById(@Argument UUID id) {
        return productService.getProductById(id);
    }

    @MutationMapping
    public ProductResponse createProduct(@Argument ProductInput input) {
        return productService.createProduct(input);
    }

    @MutationMapping
    public ProductResponse updateProduct(@Argument UUID id, @Argument ProductInput input) {
        return productService.updateProduct(id, input);
    }

    @MutationMapping
    public boolean deleteProduct(@Argument UUID id) {
        productService.deleteProduct(id);
        return true;
    }
}
