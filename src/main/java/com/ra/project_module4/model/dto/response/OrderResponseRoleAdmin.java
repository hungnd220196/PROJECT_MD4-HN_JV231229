package com.ra.project_module4.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderResponseRoleAdmin {
    private String serialNumber;

    private String user;

    private Double totalPrice;

    private String status;

    private String note;

    private String receiveName;

    private String receiveAddress;

    private String receivePhone;

    private Date createdAt;

    private Date receivedAt;
}
