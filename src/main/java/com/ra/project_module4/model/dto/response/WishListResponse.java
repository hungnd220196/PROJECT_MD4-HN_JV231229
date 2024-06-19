package com.ra.project_module4.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishListResponse {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productCategory;
}
