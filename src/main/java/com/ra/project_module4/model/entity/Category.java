package com.ra.project_module4.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "category_id")
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String categoryName;

    private String description;

    @Column(nullable = false)
    private Boolean status;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Product> products;
}

