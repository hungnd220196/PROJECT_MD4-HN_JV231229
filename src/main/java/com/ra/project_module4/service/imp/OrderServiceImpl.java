package com.ra.project_module4.service.imp;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.response.OrderResponse;
import com.ra.project_module4.model.dto.response.OrderResponseRoleAdmin;
import com.ra.project_module4.model.entity.Order;
import com.ra.project_module4.model.entity.OrderDetail;
import com.ra.project_module4.model.entity.OrderStatusName;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.OrderDetailRepository;
import com.ra.project_module4.repository.OrderRepository;
import com.ra.project_module4.security.principals.CustomUserDetail;
import com.ra.project_module4.service.OrderService;
import com.ra.project_module4.service.UserService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public OrderResponse getOrderResponse(CustomUserDetail userDetailsCustom) {
        User user = userService.findById(userDetailsCustom.getId());
        OrderResponse orderResponse;
        Order order = orderRepository.findFirstByUserOrderByCreatedAtDesc(user);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrder(order);
        orderResponse = OrderResponse.builder()
                .serialNumber(order.getSerialNumber())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .note(order.getNote())
                .receiveName(order.getReceiveName())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .build();
        Map<String, Integer> map = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            map.put(orderDetail.getProduct().getProductName(), orderDetail.getOrderQuantity());

        }
        orderResponse.setOrderItem(map);
        return orderResponse;
    }

    @Override
    public Page<OrderResponseRoleAdmin> getAllOrderRoleAdmin(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return getOrderResponseRoleAdmins(pageable,orderPage);
    }

    private Page<OrderResponseRoleAdmin> getOrderResponseRoleAdmins(Pageable pageable, Page<Order> orderRepositoryByStatusOrderStatusName) {
        List<OrderResponseRoleAdmin> orderResponseRoleAdmins = new ArrayList<>();
        for (Order order : orderRepositoryByStatusOrderStatusName) {
            OrderResponseRoleAdmin orderResponseRoleAdmin = OrderResponseRoleAdmin.builder()
                    .status(order.getStatus().name())
                    .serialNumber(order.getSerialNumber())
                    .receiveAddress(order.getReceiveAddress())
                    .totalPrice(order.getTotalPrice())
                    .createdAt(order.getCreatedAt())
                    .note(order.getNote())
                    .receivedAt(order.getReceivedAt())
                    .receiveName(order.getReceiveName())
                    .user(order.getUser().getFullname())
                    .receivePhone(order.getReceivePhone())
                    .build();
            orderResponseRoleAdmins.add(orderResponseRoleAdmin);
        }
        return new PageImpl<>(orderResponseRoleAdmins, pageable, orderRepositoryByStatusOrderStatusName.getTotalElements());
    }

    @Override
    public Page<OrderResponseRoleAdmin> findByStatusOrderStatusName(String status, Pageable pageable) throws DataExistException {
        return null;
    }

    @Override
    public OrderResponseRoleAdmin getOrderById(Long id) {
        return null;
    }

    @Override
    public OrderResponseRoleAdmin updateOrderStatusById(Long orderId, OrderStatusName status) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new NoSuchElementException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);

        return null;
    }

    @Override
    public List<OrderResponse> findByUser(CustomUserDetail userDetailsCustom, Pageable pageable) throws DataExistException {
        return List.of();
    }

    @Override
    public OrderResponse findBySerialNumber(String serialNumber, CustomUserDetail userDetailsCustom) {
        Order order = orderRepository.findBySerialNumber(serialNumber);
        if (order == null || !order.getUser().equals(userService.findById(userDetailsCustom.getId()))) {
            throw new NoSuchElementException("Không tồn tại order!");
        }
        return OrderResponse.builder()
                .serialNumber(order.getSerialNumber())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .note(order.getNote())
                .receiveName(order.getReceiveName())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .build();
    }

    @Override
    public List<OrderResponse> findByUserAndStatusOrderStatusName(CustomUserDetail userDetailsCustom, String status, Pageable pageable) throws DataExistException {
        OrderStatusName orderStatusName = switch (status) {
            case "WAITING" -> OrderStatusName.WAITING;
            case "CONFIRM" -> OrderStatusName.CONFIRM;
            case "DELIVERY" -> OrderStatusName.DELIVERY;
            case "SUCCESS" -> OrderStatusName.SUCCESS;
            case "CANCEL" -> OrderStatusName.CANCEL;
            case "DENIED" -> OrderStatusName.DENIED;
            default -> throw new DataExistException("Yêu cầu không hợp lệ, hãy kiểm tra lại", "Lỗi");
        };

        Page<Order> orderPage = orderRepository.findByUserAndStatus(userService.findById(userDetailsCustom.getId()), orderStatusName, pageable);
        if (!orderPage.hasContent()) {
            throw new DataExistException("Lịch sử mua hàng của bạn đang trống", "Lỗi");
        }
        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : orderPage) {
            OrderResponse orderResponse = OrderResponse.builder()
                    .serialNumber(order.getSerialNumber())
                    .totalPrice(order.getTotalPrice())
                    .status(order.getStatus().name())
                    .note(order.getNote())
                    .receiveName(order.getReceiveName())
                    .receiveAddress(order.getReceiveAddress())
                    .receivePhone(order.getReceivePhone())
                    .createdAt(order.getCreatedAt())
                    .receivedAt(order.getReceivedAt())
                    .build();
            orderResponseList.add(orderResponse);
        }
        return orderResponseList;
    }
}
