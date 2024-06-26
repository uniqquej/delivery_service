package org.delivery.storeadmin.domain.storemenu.converter;

import org.delivery.db.store.StoreEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.storeadmin.domain.storemenu.controller.model.MenuRegisterRequest;
import org.delivery.storeadmin.domain.storemenu.controller.model.StoreMenuResponse;
import org.springframework.stereotype.Service;

@Service
public class StoreMenuConverter {
    public StoreMenuResponse toResponse(StoreMenuEntity storeMenuEntity, StoreEntity store){

        return StoreMenuResponse.builder()
                .id(storeMenuEntity.getId())
                .store(store)
                .name(storeMenuEntity.getName())
                .status(storeMenuEntity.getStatus())
                .price(storeMenuEntity.getPrice())
                .thumbnailUrl(storeMenuEntity.getThumbnailUrl())
                .build();
    }

    public StoreMenuEntity toEntity(MenuRegisterRequest request, String imgUrl){
        return StoreMenuEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .thumbnailUrl(imgUrl)
                .build();
    }
}
