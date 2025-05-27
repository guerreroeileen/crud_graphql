package com.example.crud_graphql.resolver;

import com.example.crud_graphql.resolver.input.CategoryInput;
import com.example.crud_graphql.resolver.response.CategoryPageResponse;
import com.example.crud_graphql.resolver.response.CategoryResponse;
import com.example.crud_graphql.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CategoryResolver {

    private final CategoryService categoryService;


    @QueryMapping
    public CategoryPageResponse categories(@Argument String name, @Argument Integer page, @Argument Integer size) {
        var result = categoryService.getAllCategories(name, page, size);
        return new CategoryPageResponse(
                result.getContent(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize(),
                result.isFirst(),
                result.isLast()
        );
    }

    @QueryMapping
    public CategoryPageResponse findAllCategories(@Argument Integer page, @Argument Integer size) {
        var result = categoryService.findAll(page, size);
        return new CategoryPageResponse(
                result.getContent(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize(),
                result.isFirst(),
                result.isLast()
        );
    }

    @QueryMapping
    public CategoryResponse categoryById(@Argument UUID id) {
        return categoryService.getCategoryById(id);
    }

    @MutationMapping
    public CategoryResponse createCategory(@Argument CategoryInput input) {
        return categoryService.createCategory(input);
    }

    @MutationMapping
    public CategoryResponse updateCategory(@Argument UUID id, @Argument CategoryInput input) {
        return categoryService.updateCategory(id, input);
    }

    @MutationMapping
    public boolean deleteCategory(@Argument UUID id) {
        categoryService.deleteCategory(id);
        return true;
    }
}
