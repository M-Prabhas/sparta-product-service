package com.training.productservice.client.impl;

import com.training.productservice.client.OrderServiceClient;
import com.training.productservice.exception.OrderServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

/**
 * Talks to the Order Service to ask whether a product is still referenced by
 * any non-terminal order, so deleteProduct can refuse to remove products that
 * are still in use.
 */
@Component
public class OrderServiceRestClient implements OrderServiceClient {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceRestClient.class);

    private final RestClient restClient;

    public OrderServiceRestClient(@Qualifier("orderServiceHttpClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public boolean hasOpenOrders(UUID productId) {
        try {
            ProductOrderCheckResponse response = restClient.get()
                    .uri("/api/v1/orders/product/{productId}/has-open", productId)
                    .retrieve()
                    .body(ProductOrderCheckResponse.class);
            return response != null && response.hasOpenOrders();
        } catch (RestClientException ex) {
            log.warn("Order Service call failed while checking open orders for product {}", productId, ex);
            throw new OrderServiceUnavailableException(
                    "Order Service is unavailable while checking open orders for product " + productId, ex);
        }
    }

    private record ProductOrderCheckResponse(UUID productId, boolean hasOpenOrders) {
    }
}
