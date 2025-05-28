package com.example.crud_graphql.resolver;

import com.example.crud_graphql.resolver.input.ProductInput;
import com.example.crud_graphql.resolver.response.CategoryResponse;
import com.example.crud_graphql.resolver.response.ProductPageResponse;
import com.example.crud_graphql.resolver.response.ProductResponse;
import com.example.crud_graphql.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureGraphQlTester
class ProductResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnPaginatedProducts() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        CategoryResponse category = new CategoryResponse(categoryId, "Tecnología");

        ProductResponse product = new ProductResponse(
                productId,
                "Teclado mecánico",
                "Teclado para gamers",
                BigDecimal.TEN,
                25,
                true,
                category
        );
        ProductPageResponse pageResponse = new ProductPageResponse(
                Collections.singletonList(product),
                1,
                1,
                0,
                10,
                true,
                true
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));

        Page<ProductResponse> responses = new PageImpl<>(Collections.singletonList(product), pageable, pageResponse.getTotalElements());

        Mockito.when(productService.getAllProducts(
                anyInt(),
                anyInt(),
                any(),
                any(),
                anyString()
        )).thenReturn(responses);

        String query = """
                    query {
                      products(page: 0, size: 10, sortDirection: "ASC") {
                        content {
                          id
                          name
                          description
                          price
                          stock
                          active
                          category {
                            id
                            name
                          }
                        }
                        totalElements
                        totalPages
                        pageNumber
                        pageSize
                        first
                        last
                      }
                    }
                """;

        graphQlTester.document(query)
                .execute()
                .path("products.content[0].name").entity(String.class).isEqualTo("Teclado mecánico")
                .path("products.totalElements").entity(Integer.class).isEqualTo(1);
    }

    @Test
    void shouldReturnProductById() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        CategoryResponse category = new CategoryResponse(categoryId, "Tecnología");

        ProductResponse product = new ProductResponse(
                productId,
                "Teclado mecánico",
                "Teclado para gamers",
                BigDecimal.TEN,
                25,
                true,
                category
        );

        Mockito.when(productService.getProductById(productId)).thenReturn(product);

        String query = String.format("""
                query {
                  productById(id: "%s") {
                    id
                    name
                    description
                    price
                    stock
                    active
                    category {
                      id
                      name
                    }
                  }
                }
                """, productId);

        graphQlTester.document(query)
                .execute()
                .path("productById.name").entity(String.class).isEqualTo("Teclado mecánico");
    }

    @Test
    void shouldCreateProduct() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductInput input = new ProductInput(
                "Teclado mecánico",
                "Teclado para gamers",
                BigDecimal.TEN,
                25,
                true,
                categoryId
        );

        CategoryResponse category = new CategoryResponse(categoryId, "Tecnología");

        ProductResponse response = new ProductResponse(
                productId,
                input.getName(),
                input.getDescription(),
                input.getPrice(),
                input.getStock(),
                input.getActive(),
                category
        );

        Mockito.when(productService.createProduct(any(ProductInput.class))).thenReturn(response);

        String mutation = String.format("""
                        mutation {
                          createProduct(input: {
                            name: "%s",
                            description: "%s",
                            price: %.2f,
                            stock: %d,
                            active: %s,
                            categoryId: "%s"
                          }) {
                            id
                            name
                            description
                            price
                            stock
                            active
                            category {
                              id
                              name
                            }
                          }
                        }
                        """,
                input.getName(),
                input.getDescription(),
                input.getPrice(),
                input.getStock(),
                input.getActive(),
                input.getCategoryId()
        );

        graphQlTester.document(mutation)
                .execute()
                .path("createProduct.name").entity(String.class).isEqualTo("Teclado mecánico");
    }

    @Test
    void shouldUpdateProduct() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductInput input = new ProductInput(
                "Teclado actualizado",
                "Teclado para gamers pro",
                BigDecimal.valueOf(15),
                30,
                true,
                categoryId
        );

        CategoryResponse category = new CategoryResponse(categoryId, "Tecnología");

        ProductResponse response = new ProductResponse(
                productId,
                input.getName(),
                input.getDescription(),
                input.getPrice(),
                input.getStock(),
                input.getActive(),
                category
        );

        Mockito.when(productService.updateProduct(any(), any(ProductInput.class))).thenReturn(response);

        String mutation = String.format("""
                        mutation {
                          updateProduct(id: "%s", input: {
                            name: "%s",
                            description: "%s",
                            price: %.2f,
                            stock: %d,
                            active: %s,
                            categoryId: "%s"
                          }) {
                            id
                            name
                            description
                            price
                            stock
                            active
                            category {
                              id
                              name
                            }
                          }
                        }
                        """,
                productId,
                input.getName(),
                input.getDescription(),
                input.getPrice(),
                input.getStock(),
                input.getActive(),
                input.getCategoryId()
        );

        graphQlTester.document(mutation)
                .execute()
                .path("updateProduct.name").entity(String.class).isEqualTo("Teclado actualizado");
    }

    @Test
    void shouldDeleteProduct() {
        UUID productId = UUID.randomUUID();

        Mockito.doNothing().when(productService).deleteProduct(productId);

        String mutation = String.format("""
                mutation {
                  deleteProduct(id: "%s")
                }
                """, productId);

        graphQlTester.document(mutation)
                .execute()
                .path("deleteProduct").entity(Boolean.class).isEqualTo(true);
    }


}