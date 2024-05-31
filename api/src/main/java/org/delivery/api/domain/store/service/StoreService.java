package org.delivery.api.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreCategory;
import org.delivery.db.store.enums.StoreStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public StoreEntity getStoreWithThrow(Long id){
        return storeRepository.findFirstByIdAndStatusOrderByIdDesc(id, StoreStatus.REGISTERED)
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }

    public StoreEntity register(StoreEntity store){
        return Optional.ofNullable(store)
                .map(it-> {
                    it.setStar(0);
                    it.setStatus(StoreStatus.REGISTERED);
                    return storeRepository.save(store);
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }

    public List<StoreEntity> searchByCategory(StoreCategory category){
        return storeRepository.findAllByStatusAndCategoryOrderByStarDesc(
                StoreStatus.REGISTERED,
                category
        );
    }

    public List<StoreEntity> registeredStore(){
        return storeRepository.findAllByStatusOrderByIdDesc(StoreStatus.REGISTERED);
    }
}