package com.training.productservice.service;

import com.training.productservice.dto.*;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto dto);

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(UUID id);

    ProductResponseDto updatePrice(UUID id, PriceUpdateRequestDto dto);

    //ProductResponseDto updateStock(UUID id, StockUpdateRequestDto dto);

    AvailabilityResponseDto checkAvailability(UUID id, Integer quantity);

    ProductResponseDto reduceStock(UUID id, Integer quantity, String orderReference);

    String deleteProduct(UUID id);

    ProductResponseDto updateProductInfo(UUID id, ProductRequestDto product);

    ProductResponseDto adjustStock(UUID id, StockUpdateRequestDto stockUpdateRequestDto);
}
