package com.training.productservice.service;

import com.training.productservice.dto.AvailabilityResponseDto;
import com.training.productservice.dto.ProductAvailability;
import com.training.productservice.entity.Product;
import com.training.productservice.exception.InsufficientStockException;
import com.training.productservice.exception.ProductNotFoundException;
import com.training.productservice.repository.ProductRepository;
import com.training.productservice.util.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private Product buildProduct(UUID id, int stock) {
        return Product.builder()
                .id(id)
                .productName("Test Product")
                .category("Electronics")
                .price(BigDecimal.valueOf(99.99))
                .stockQuantity(stock)
                .active(true)
                .build();
    }

    // Positive: stock is sufficient for requested quantity
    @Test
    void checkAvailability_whenStockSufficient_returnsAvailable() {
        UUID id = UUID.randomUUID();
        Product product = buildProduct(id, 10);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        AvailabilityResponseDto result = productServiceImpl.checkAvailability(id, 5);

        assertEquals(id, result.getProductId());
        assertEquals(5, result.getRequestedQuantity());
        assertEquals(10, result.getCurrentStock());
        assertEquals(ProductAvailability.AVAILABLE, result.getAvailable());
        verify(productRepository, times(1)).findById(id);
    }

    // Positive: stock exactly equals requested quantity — boundary case
    @Test
    void checkAvailability_whenStockEqualsRequestedQuantity_returnsAvailable() {
        UUID id = UUID.randomUUID();
        Product product = buildProduct(id, 5);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        AvailabilityResponseDto result = productServiceImpl.checkAvailability(id, 5);

        assertEquals(ProductAvailability.AVAILABLE, result.getAvailable());
        assertEquals(5, result.getCurrentStock());
        assertEquals(5, result.getRequestedQuantity());
        verify(productRepository, times(1)).findById(id);
    }

    // Positive: stock is less than requested quantity — returns NOTAVAILABLE (not an exception)
    @Test
    void checkAvailability_whenStockLessThanRequestedQuantity_returnsNotAvailable() {
        UUID id = UUID.randomUUID();
        Product product = buildProduct(id, 3);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        AvailabilityResponseDto result = productServiceImpl.checkAvailability(id, 10);

        assertEquals(id, result.getProductId());
        assertEquals(10, result.getRequestedQuantity());
        assertEquals(3, result.getCurrentStock());
        assertEquals(ProductAvailability.NOTAVAILABLE, result.getAvailable());
        verify(productRepository, times(1)).findById(id);
    }

    // Negative: product ID does not exist in repository
    @Test
    void checkAvailability_whenProductNotFound_throwsProductNotFoundException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> productServiceImpl.checkAvailability(id, 5));

        assertTrue(ex.getMessage().contains(id.toString()));
        verify(productRepository, times(1)).findById(id);
    }

    // Negative: product exists but stock is zero
    @Test
    void checkAvailability_whenStockIsZero_throwsInsufficientStockException() {
        UUID id = UUID.randomUUID();
        Product product = buildProduct(id, 0);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        InsufficientStockException ex = assertThrows(InsufficientStockException.class,
                () -> productServiceImpl.checkAvailability(id, 5));

        assertTrue(ex.getMessage().contains(id.toString()));
        verify(productRepository, times(1)).findById(id);
    }
}
