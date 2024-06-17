package com.ra.project_module4.service.imp;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.model.entity.ShoppingCart;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.ProductRepository;
import com.ra.project_module4.repository.ShoppingCartRepository;
import com.ra.project_module4.repository.UserRepository;
import com.ra.project_module4.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private UserRepository UserRepository;
    @Autowired
    private ShoppingCartRepository ShoppingCartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ShoppingCart> findByUser(User user) {
        return shoppingCartRepository.findByUser(user);
    }

    @Override
    public ShoppingCart findById(Long id) {
        return shoppingCartRepository.findById(id).orElseThrow(() -> new RuntimeException("shopping cart not found"));
    }

    @Override
    public void saveShoppingCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found"));
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);
        boolean check = false; //mac dinh la chua co san pham
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getProduct().equals(product)) {
                int orderQuantity = cart.getOrderQuantity();
                cart.setOrderQuantity(orderQuantity + 1);
                shoppingCartRepository.save(cart);
                check = true; //sp co trong gio hang
                break;
            }
        }
        if (!check) { // sp chua co trong gio hang
            ShoppingCart cart = new ShoppingCart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setOrderQuantity(1);
            shoppingCartRepository.save(cart);
        }

    }

    @Override
    public void changeQuantity(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found"));
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);

        boolean check = false;
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getProduct().equals(product)) {
                int orderQuantity = cart.getOrderQuantity();
                cart.setOrderQuantity(quantity);
                shoppingCartRepository.save(cart);
            }
        }
        if (!check) {
            ShoppingCart cart = new ShoppingCart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setOrderQuantity(quantity);
            shoppingCartRepository.save(cart);
        }
    }

}
