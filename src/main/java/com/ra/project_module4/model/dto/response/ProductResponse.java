package com.ra.project_module4.model.dto.response;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductResponse {
    private Long productId;

    private String sku;

    private String productName;

    private String description;

    private Double unitPrice;

    private Integer stockQuantity;

    private String image;

    private String category;

    private Date createdAt;

    private Date updatedAt;
}
