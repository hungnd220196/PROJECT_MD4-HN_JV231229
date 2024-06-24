package com.ra.project_module4.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormProductRequest {

    @NotBlank(message = "productName is empty")
    private String productName;

    @NotBlank(message = "description is empty")
    @Size(max = 100)
    private String description;

    @NotNull(message ="unitPrice is empty" )
    @Min(value = 0, message = "price must be greater than or equal to 0")
    private Double unitPrice;

    @NotNull(message ="stockQuantity is empty" )
    private Integer stockQuantity;

    private String image;

    @NotNull(message ="category is empty" )
    private Long category;
}
