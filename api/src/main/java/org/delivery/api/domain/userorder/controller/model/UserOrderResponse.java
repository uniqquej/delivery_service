package org.delivery.api.domain.userorder.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.db.userorder.enums.UserOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderResponse {
    private Long id;
    private Long storeId;
    private String address;
    private boolean hasReview;
    private UserOrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime orderedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime cookingStartedAt;
    private LocalDateTime deliveryStartedAt;
    private LocalDateTime receivedAt;



}
