package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.Category;
import com.ra.project_module4.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findFirst10ByOrderByCreatedAtDesc();

    List<Product> findByCategory(Category category);

    boolean existsByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:searchTerm% OR p.description LIKE %:searchTerm%")
    List<Product> findByNameOrDescriptionContaining(@Param("searchTerm") String searchTerm);

}
