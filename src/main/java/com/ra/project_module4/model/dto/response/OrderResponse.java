package com.ra.project_module4.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderResponse {
    private String serialNumber;

    private Double totalPrice;

    private String status;

    private String note;

    private String receiveName;

    private String receiveAddress;

    private String receivePhone;

    private Date createdAt;

    private Date receivedAt;

    private Map<String, Integer> orderItem;
}
