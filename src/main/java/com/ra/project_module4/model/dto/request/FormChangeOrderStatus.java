package com.ra.project_module4.model.dto.request;

import com.ra.project_module4.model.entity.OrderStatusName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormChangeOrderStatus {
    private OrderStatusName orderStatusName;
}
