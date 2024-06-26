package org.delivery.storeadmin.domain.storemenu.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.storemenu.enums.StoreMenuStatusEnum;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreMenuResponse {
    private Long id;

    private StoreEntity store;

    private String name;

    private BigDecimal price;

    private StoreMenuStatusEnum status;

    private String thumbnailUrl;
}