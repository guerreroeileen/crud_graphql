package com.example.crud_graphql.resolver;

import com.example.crud_graphql.resolver.response.CategoryResponse;
import com.example.crud_graphql.resolver.response.ProductPageResponse;
import com.example.crud_graphql.resolver.response.ProductResponse;
import com.example.crud_graphql.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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

        Pageable pageable = Pageable.unpaged();

        Page<ProductResponse> responses = new PageImpl<>(Collections.singletonList(product), pageable, pageResponse.getTotalElements());

        System.out.println("this is------------------"+responses);

        Mockito.when(productService.getAllProducts(
                any(),
                any(),
                anyString(),
                any(),
                anyString()
        )).thenReturn(responses);

        String query = """
                    query {
                      products(page: 0, size: 10) {
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


}