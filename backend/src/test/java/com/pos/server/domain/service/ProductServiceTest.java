package com.pos.server.domain.service;

import com.pos.server.domain.model.Product;
import com.pos.server.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(10.99);
        testProduct.setStock(100);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.getAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getAll();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals(testProduct.getName(), actualProducts.get(0).getName());
        verify(productRepository, times(1)).getAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(productRepository.getProduct(anyInt())).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> result = productService.getProduct(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testProduct.getName(), result.get().getName());
        verify(productRepository, times(1)).getProduct(1);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldReturnEmpty() {
        // Arrange
        when(productRepository.getProduct(anyInt())).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.getProduct(999);

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).getProduct(999);
    }

    @Test
    void saveProduct_ShouldReturnSavedProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product savedProduct = productService.save(testProduct);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(testProduct.getName(), savedProduct.getName());
        verify(productRepository, times(1)).save(testProduct);
    }
}