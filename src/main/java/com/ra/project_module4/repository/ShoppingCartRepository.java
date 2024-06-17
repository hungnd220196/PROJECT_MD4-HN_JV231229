package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.model.entity.ShoppingCart;
import com.ra.project_module4.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUser(User user);
    Optional<ShoppingCart> findByUserAndProduct(User user, Product product);
}
