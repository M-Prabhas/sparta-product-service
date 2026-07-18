package com.training.productservice.repository;

import com.training.productservice.entity.Product;
import com.training.productservice.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByCategory(String category);

    List<Product> findByStatusNot(ProductStatus status);

    boolean existsByProductNameIgnoreCaseAndCategoryIgnoreCase(String productName, String category);
}
