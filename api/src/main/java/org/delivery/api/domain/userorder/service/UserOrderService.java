package org.delivery.api.domain.userorder.service;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.UserErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;
import org.delivery.db.user.UserRepository;
import org.delivery.db.user.enums.UserStatus;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userorder.UserOrderRepository;
import org.delivery.db.userorder.enums.UserOrderStatus;
import org.delivery.db.userordermenu.UserOrderMenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserOrderService {
    private final UserOrderRepository userOrderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public UserOrderEntity order(User user, List<UserOrderMenuEntity> userOrderMenuEntityList, UserOrderRequest request){
        var userEntity = userRepository.findFirstByIdAndStatusOrderByIdDesc(user.getId(), UserStatus.REGISTERED)
                .orElseThrow(()->new ApiException(UserErrorCode.USER_NOT_FOUND));

        var storeEntity = storeRepository.findFirstByIdAndStatusOrderByIdDesc(request.getStoreId(), StoreStatus.REGISTERED)
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));

        var orderEntity =  UserOrderEntity.createUserOrder(userEntity, userOrderMenuEntityList);
        orderEntity.setOrderedAt(LocalDateTime.now());
        orderEntity.setStatus(UserOrderStatus.REGISTERED);
        orderEntity.setTotalPrice(orderEntity.getTotalPrice());
        orderEntity.setStore(storeEntity);
        orderEntity.setAddress(request.getAddress());
        userOrderRepository.save(orderEntity);
        return orderEntity;
    }

    @Transactional
    public UserOrderEntity orderSuccess(UserOrderEntity userOrderEntity){
        userOrderEntity.setStatus(UserOrderStatus.ORDER);
        return userOrderEntity;
    }

    public UserOrderEntity findOrderByIdAndUser(Long id, Long userId){
        var userOrder = userOrderRepository.findOrderByIdAndUser(userId, id)
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));

        return userOrder;
    }
    public Page<UserOrderEntity> getUserOrderList(Long userId, List<UserOrderStatus> statusList, PageRequest pageRequest){

        return userOrderRepository.findOrderWithOrderMenu(userId, statusList, pageRequest);
    }

    public Page<UserOrderEntity> currentOrders(Long userId,Pageable pageable){
        int page = pageable.getPageNumber()-1;
        int pageSize = 2;
        // 현재 진행 중인 주문
        var statusList = List.of(UserOrderStatus.ORDER,UserOrderStatus.COOKING,UserOrderStatus.ACCEPT,UserOrderStatus.DELIVERY) ;
        return getUserOrderList(userId,statusList,PageRequest.of(page, pageSize));
    }

    public Page<UserOrderEntity> historyOrders(Long userId,Pageable pageable){
        int pageSize = 2;
        int page = pageable.getPageNumber()-1;
        //완료된 주문
        var statusList = List.of(UserOrderStatus.RECEIVE) ;
        return getUserOrderList(userId,statusList,PageRequest.of(page, pageSize));
    }

    public UserOrderEntity order(UserOrderEntity userOrderEntity){
        return Optional.ofNullable(userOrderEntity)
                .map(it->{
                    it.setOrderedAt(LocalDateTime.now());
                    it.setStatus(UserOrderStatus.ORDER);
                    return userOrderRepository.save(it);
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }

    public UserOrderEntity setStatus(UserOrderEntity userOrderEntity, UserOrderStatus status){
        userOrderEntity.setStatus(status);
        return userOrderRepository.save(userOrderEntity);
    }

    public UserOrderEntity receive(UserOrderEntity userOrderEntity){
        return Optional.ofNullable(userOrderEntity)
                .map(it->{
                    it.setReceivedAt(LocalDateTime.now());
                    return setStatus(it, UserOrderStatus.RECEIVE);
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }

}
