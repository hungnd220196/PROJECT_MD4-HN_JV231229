package com.ra.project_module4.service.imp;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.entity.*;
import com.ra.project_module4.repository.*;
import com.ra.project_module4.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

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
    public void changeQuantity(Long userId, Long cartItemId, Integer quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        ShoppingCart cartItems = shoppingCartRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart Item Not Found"));
        Product product = cartItems.getProduct();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);

        boolean check = false;
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getProduct().equals(product)) {
                int orderQuantity = cart.getOrderQuantity();
                if (quantity <= product.getStockQuantity()) {
                    cart.setOrderQuantity(quantity + cart.getOrderQuantity());
                    shoppingCartRepository.save(cart);
                    check = true;
                } else {
                    throw new RuntimeException("Requested quantity exceeds available stock");
                }

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

    @Override
    public void deleteShoppingCart(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found"));
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);
        boolean check = false;
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getProduct().equals(product)) {
                shoppingCartRepository.delete(cart);
            }
        }
    }

    @Override
    public void deleteShoppingCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);
        boolean check = false;
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getUser().equals(user)) {
                shoppingCartRepository.delete(cart);
            }
        }
    }

    @Override
    public ShoppingCart checkout(Long userId) throws DataExistException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUser(user);
        if (shoppingCarts.isEmpty()) {
            throw new DataExistException("Giỏ hàng của bạn đang rỗng", "Lỗi");

        }
        double totalPrice = 0;
        for (ShoppingCart cart : shoppingCarts) {
            Integer orderQuantity = cart.getOrderQuantity();
            Product item = cart.getProduct();
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("Product Not Found"));

            if (cart.getOrderQuantity() > product.getStockQuantity()) {
                throw new DataExistException("Số lượng sản phẩm trong kho không đủ cho đơn hàng của bạn", "Lỗi");
            }
            totalPrice += cart.getProduct().getUnitPrice() * cart.getOrderQuantity();
        }
// Nếu tất cả các sản phẩm trong Giỏ hàng có đủ số lượng trong kho
        Order order = new Order();
        order.setSerialNumber(UUID.randomUUID().toString());
        order.setUser(user);
        order.setStatus(OrderStatusName.WAITING);
        order.setNote("Thank you!!!");
        order.setReceiveName(user.getFullname());
        order.setReceiveAddress(user.getAddress());
        order.setReceivePhone(user.getPhone());
        order.setCreatedAt(new Date());
        // Add 4 days to the current date for receivedAt
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Set it to the current date
        calendar.add(Calendar.DAY_OF_YEAR, 4); // Add 4 days
        Date receivedAtDate = calendar.getTime(); // Get the updated date
        order.setReceivedAt(receivedAtDate);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        for (ShoppingCart shoppingCart : shoppingCarts) {
            Integer cartItemQuantity = shoppingCart.getOrderQuantity(); // Số lượng người ta mua
            Product product = productRepository.findById(shoppingCart.getProduct().getProductId()).orElseThrow(() -> new NoSuchElementException("Sp không tồn tại"));
            Integer productQuantityRepo = product.getStockQuantity(); // Lấy ra số lượng trong kho
            Integer stockQuantity = productQuantityRepo - cartItemQuantity;
            product.setStockQuantity(stockQuantity);
            productRepository.save(product);

            shoppingCartRepository.deleteById(shoppingCart.getShoppingCartId()); // Reset lại Giỏ hàng, chuyển nó vào OrderDetail

            OrderDetail orderDetail = new OrderDetail();
            OrderDetailCompositekey orderDetailId = new OrderDetailCompositekey();
            orderDetailId.setOrderId(order.getOrderId());
            orderDetailId.setProductId(shoppingCart.getProduct().getProductId());
            orderDetail.setOrderDetailId(orderDetailId);

            orderDetail.setOrder(order);
            orderDetail.setProduct(shoppingCart.getProduct());
            orderDetail.setOrderQuantity(shoppingCart.getOrderQuantity());
            double orderDetailPrice = shoppingCart.getProduct().getUnitPrice() * shoppingCart.getOrderQuantity();
            orderDetail.setUnitPrice(orderDetailPrice);
            orderDetailRepository.save(orderDetail);
        }
        return new ShoppingCart();
    }

}
