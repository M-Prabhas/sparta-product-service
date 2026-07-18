package com.training.productservice.dto;

import com.training.productservice.enums.ProductAvailability;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponseDto {

    private UUID productId;
    private Integer requestedQuantity;
    private ProductAvailability available;
    private Integer currentStock;
}
