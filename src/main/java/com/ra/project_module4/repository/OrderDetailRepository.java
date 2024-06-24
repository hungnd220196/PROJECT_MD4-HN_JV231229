package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.Order;
import com.ra.project_module4.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
    @Query("select od from OrderDetail od where od.orderDetailId.orderId=:orderId")
    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
}
