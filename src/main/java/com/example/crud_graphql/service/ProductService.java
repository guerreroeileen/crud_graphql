package com.example.crud_graphql.service;


import com.example.crud_graphql.dto.ProductDto;
import com.example.crud_graphql.exceptions.ResourceNotFoundException;
import com.example.crud_graphql.model.Product;
import com.example.crud_graphql.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public Page<ProductDto> getAllProducts(Integer page, Integer size, String productName, UUID categoryId, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, "price");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productsPage = productRepository.findByNameAndCategoryId(productName, categoryId, pageable);

        return productsPage.map(product-> this.modelMapper.map(product, ProductDto.class));
    }

    public ProductDto getProductById(UUID id) {
        return productRepository.findById(id)
                .map(product -> this.modelMapper.map(product, ProductDto.class))
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getName(), id));
    }

    public ProductDto createProduct(ProductDto productDTO) {
        Product product = this.modelMapper.map(productDTO, Product.class);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    public ProductDto updateProduct(UUID id, ProductDto productDTO) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    Product product = this.modelMapper.map(productDTO, Product.class);
                    product.setId(existingProduct.getId());
                    return this.modelMapper.map(productRepository.save(product), ProductDto.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getName(), id));
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }


}
