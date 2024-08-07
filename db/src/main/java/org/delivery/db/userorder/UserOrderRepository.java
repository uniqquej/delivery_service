package org.delivery.db.userorder;

import org.delivery.db.userorder.enums.UserOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserOrderRepository extends JpaRepository<UserOrderEntity,Long> {
    List<UserOrderEntity> findAllByStoreIdAndStatusOrderByIdDesc(Long storeId, UserOrderStatus status);

    @Query("SELECT o FROM UserOrderEntity o JOIN FETCH o.orderMenus WHERE o.store.id = :storeId" +
            " AND o.status in :statusList ORDER BY o.id DESC")
    List<UserOrderEntity> findAllByStoreIdAndStatusInOrderByIdDesc(@Param("storeId") Long storeId, @Param("statusList") List<UserOrderStatus> status);

    @Query(value="SELECT o FROM UserOrderEntity o JOIN FETCH o.orderMenus WHERE o.user.id = :userId AND o.status in :statusList ORDER BY o.id DESC",
            countQuery = "SELECT o FROM UserOrderEntity o WHERE o.user.id = :userId AND o.status in :statusList")
    Page<UserOrderEntity> findOrderWithOrderMenu(
            @Param("userId") Long userId,
            @Param("statusList") List<UserOrderStatus> statusList,
            PageRequest pageRequest
    );

    @Query("SELECT o FROM UserOrderEntity o JOIN FETCH o.orderMenus WHERE o.user.id = :userId AND o.id = :orderId")
    Optional<UserOrderEntity> findOrderByIdAndUser(
            @Param("userId") Long userId, @Param("orderId") Long id);

    @Query("SELECT o FROM UserOrderEntity o LEFT JOIN FETCH o.orderMenus WHERE o.id = :orderId")
    Optional<UserOrderEntity> findOrderById(@Param("orderId") Long id);

}
