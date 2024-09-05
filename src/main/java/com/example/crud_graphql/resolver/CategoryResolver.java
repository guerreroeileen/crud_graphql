package com.example.crud_graphql.resolver;

import com.example.crud_graphql.resolver.input.CategoryInput;
import com.example.crud_graphql.resolver.response.CategoryResponse;
import com.example.crud_graphql.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryResolver {

    private final CategoryService categoryService;


    @QueryMapping
    public List<CategoryResponse> categories(@Argument String name,@Argument Integer page,@Argument Integer size) {
        return categoryService.getAllCategories(name, page, size);
    }

    @QueryMapping
    public CategoryResponse category(@Argument UUID id) {
        return categoryService.getCategoryById(id);
    }

    @MutationMapping
    public CategoryResponse createCategory(@Argument CategoryInput input) {
        return categoryService.createCategory(input);
    }

    @MutationMapping
    public CategoryResponse updateCategory(UUID id, CategoryInput input) {
        return categoryService.updateCategory(id, input);
    }

    @MutationMapping
    public boolean deleteCategory(@Argument UUID id) {
        categoryService.deleteCategory(id);
        return true;
    }
}
