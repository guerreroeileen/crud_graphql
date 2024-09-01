package com.example.crud_graphql.service;

import com.example.crud_graphql.dto.CategoryDto;
import com.example.crud_graphql.exceptions.ResourceNotFoundException;
import com.example.crud_graphql.model.Category;
import com.example.crud_graphql.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryDto> getAllCategories(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage;
        if (name != null && !name.isEmpty()) {
            categoryPage = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }
        return categoryPage.stream()
                .map(this::apply)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::apply)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class.getName(), id));
    }

    public CategoryDto createCategory(CategoryDto categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        return this.apply(categoryRepository.save(category));
    }

    public CategoryDto updateCategory(UUID id, CategoryDto categoryDTO) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    Category category = modelMapper.map(categoryDTO, Category.class);
                    category.setId(existingCategory.getId());
                    return this.apply(categoryRepository.save(category));
                })
                .orElseThrow(() -> new ResourceNotFoundException(Category.class.getName(), id));
    }

    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDto apply(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
