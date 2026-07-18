package com.training.productservice.client;

import java.util.UUID;

public interface OrderServiceClient {

    boolean hasOpenOrders(UUID productId);
}
