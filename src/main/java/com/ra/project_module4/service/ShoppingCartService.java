package com.ra.project_module4.service;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.entity.ShoppingCart;
import com.ra.project_module4.model.entity.User;
import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCart> findByUser(User user);
    ShoppingCart findById(Long id);
    void saveShoppingCart(Long userId, Long productId);
    void changeQuantity(Long userId, Long cartItemId, Integer quantity);
    void deleteShoppingCart(Long userId, Long productId);
    void deleteShoppingCart(Long userId);
    ShoppingCart checkout(Long userId) throws DataExistException;
}
