package com.ra.project_module4.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailCompositekey implements Serializable {
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;
}

