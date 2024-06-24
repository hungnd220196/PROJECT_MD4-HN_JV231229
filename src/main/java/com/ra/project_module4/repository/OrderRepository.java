package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.Order;
import com.ra.project_module4.model.entity.OrderStatusName;
import com.ra.project_module4.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findFirstByUserOrderByCreatedAtDesc(User user);

    Page<Order> findByStatus(OrderStatusName status, Pageable pageable);

    Page<Order> findByUser(User user, Pageable pageable);

    Order findBySerialNumber(String serialNumber);

    Page<Order> findByUserAndStatus(User user, OrderStatusName status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
