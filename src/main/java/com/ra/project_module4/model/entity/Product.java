package com.ra.project_module4.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, unique = true, length = 100)

    private String sku = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true, length = 100)
    private String productName;

    private String description;
    private Boolean status;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    private Category category;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}

