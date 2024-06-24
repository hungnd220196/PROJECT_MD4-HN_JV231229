package com.ra.project_module4.service;


import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.response.OrderResponse;
import com.ra.project_module4.model.dto.response.OrderResponseRoleAdmin;
import com.ra.project_module4.model.entity.Order;
import com.ra.project_module4.model.entity.OrderStatusName;
import com.ra.project_module4.security.principals.CustomUserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface OrderService  {
    Page<Order> findAll(Pageable pageable);
    OrderResponse getOrderResponse(CustomUserDetail userDetailsCustom);

    Page<OrderResponseRoleAdmin> getAllOrderRoleAdmin(Pageable pageable);

    Page<OrderResponseRoleAdmin> findByStatusOrderStatusName(String status, Pageable pageable) throws DataExistException;

    OrderResponseRoleAdmin getOrderById(Long id);

    OrderResponseRoleAdmin updateOrderStatusById(Long orderId, OrderStatusName status);

    List<OrderResponse> findByUser(CustomUserDetail userDetailsCustom, Pageable pageable) throws DataExistException;

    OrderResponse findBySerialNumber(String serialNumber, CustomUserDetail userDetailsCustom);

    List<OrderResponse> findByUserAndStatusOrderStatusName(CustomUserDetail userDetailsCustom, String status, Pageable pageable) throws DataExistException;
    Order cancelOrder(Long orderId) ;
}
