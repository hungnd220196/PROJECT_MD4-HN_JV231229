package com.ra.project_module4.controller;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.request.FormAddToCartRequest;
import com.ra.project_module4.model.dto.request.FormChangePasswordRequest;
import com.ra.project_module4.model.dto.request.FormChangeQuantityCartItem;
import com.ra.project_module4.model.dto.request.FormEditUserRequest;
import com.ra.project_module4.model.dto.response.OrderResponse;
import com.ra.project_module4.model.dto.response.ResponseDtoSuccess;
import com.ra.project_module4.model.dto.response.ShoppingCartResponse;
import com.ra.project_module4.model.dto.response.UserDetailResponse;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.model.entity.ShoppingCart;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.security.principals.CustomUserDetail;
import com.ra.project_module4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class UserController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;

    //hien thi san pham trong gio hang
    @GetMapping("/cart/list")
    public ResponseEntity<?> getCartList(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        User user = userService.findById(customUserDetail.getId());
        List<ShoppingCart> cartList = shoppingCartService.findByUser(user);
        Map<String, Integer> map = new HashMap<>();
        for (ShoppingCart cart : cartList) {
            map.put(cart.getProduct().getProductName(), cart.getOrderQuantity());
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(map);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(shoppingCartResponse, HttpStatus.OK), HttpStatus.OK);

    }

    //them san pham vao gio hang
    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal CustomUserDetail userDetails, @RequestBody FormAddToCartRequest formAddToCartRequest) {
        shoppingCartService.saveShoppingCart(userDetails.getId(), formAddToCartRequest.getProductId());
        User user = userService.findById(userDetails.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.findByUser(user);
        Map<String, Integer> map = new HashMap<>();
        for (ShoppingCart cart : shoppingCartList) {
            map.put(cart.getProduct().getProductName(), cart.getOrderQuantity());
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(map);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(shoppingCartResponse, HttpStatus.OK), HttpStatus.OK);
    }

    //chuyen doi so luong gio hang
    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> changeQuantityCart(@AuthenticationPrincipal CustomUserDetail customUserDetail, @PathVariable Long cartItemId, @RequestBody FormChangeQuantityCartItem formChangeQuantityCartItem) {
        shoppingCartService.changeQuantity(customUserDetail.getId(), cartItemId, formChangeQuantityCartItem.getCartItemQuantity());
        User user = userService.findById(customUserDetail.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.findByUser(user);
        Map<String, Integer> map = new HashMap<>();
        for (ShoppingCart cart : shoppingCartList) {
            map.put(cart.getProduct().getProductName(), cart.getOrderQuantity());
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(map);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(shoppingCartResponse, HttpStatus.OK), HttpStatus.OK);

    }

    // xoa 1 sp
    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> getCartItem(@AuthenticationPrincipal CustomUserDetail customUserDetail, @PathVariable Long cartItemId) {
        shoppingCartService.deleteShoppingCart(customUserDetail.getId(), shoppingCartService.findById(cartItemId).getProduct().getProductId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // xoa tat ca sp
    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        shoppingCartService.deleteShoppingCart(customUserDetail.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //  Đặt hàng - /api.myservice.com/v1/user/cart/checkout
    @PostMapping("/cart/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal CustomUserDetail userDetailsCustom) throws DataExistException {
        ShoppingCart shoppingCart = shoppingCartService.checkout(userDetailsCustom.getId());
        Map<String, OrderResponse> map = new HashMap<>();
        OrderResponse orderResponse = orderService.getOrderResponse(userDetailsCustom);
        map.put("Đặt hàng thành công", orderResponse);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(map, HttpStatus.OK), HttpStatus.OK);
    }

    //  Thông tin tài khoản người dùng - /api.myservice.com/v1/user/account
    @GetMapping("/account")
    public ResponseEntity<?> getUserDetail(@AuthenticationPrincipal CustomUserDetail userDetailsCustom) {
        UserDetailResponse userDetailResponse = userService.getUserDetail(userDetailsCustom);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(userDetailResponse, HttpStatus.OK), HttpStatus.OK);
    }


    //lay ra danh sach dia chi user
    @GetMapping("/account/addresses")
    public ResponseEntity<?> getUserAddresses(@AuthenticationPrincipal CustomUserDetail userDetailsCustom) {

        return new ResponseEntity<>(new ResponseDtoSuccess<>(addressService.findAddressByUser(userDetailsCustom.getId()), HttpStatus.OK), HttpStatus.OK);
    }
    // lay dia chi nguoi dung bang addressId
    @GetMapping("/account/addresses/{addressId}")
    public ResponseEntity<?> getUserAddress(@PathVariable Long addressId) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(addressService.getAddressByID(addressId), HttpStatus.OK), HttpStatus.OK);
    }

    //  Cập nhật thông tin người dùng - /api.myservice.com/v1/user/account
    @PutMapping("/account")
    public ResponseEntity<?> updateUserDetail(@AuthenticationPrincipal CustomUserDetail userDetailsCustom, @ModelAttribute FormEditUserRequest formEditUserRequest) throws IOException {
        // Cập nhật thông tin người dùng
        UserDetailResponse userDetailResponse = userService.editUserDetail(userDetailsCustom, formEditUserRequest);

        return new ResponseEntity<>(new ResponseDtoSuccess<>(userDetailResponse, HttpStatus.OK), HttpStatus.OK);
    }

    //  Thay đổi mật khẩu (payload : oldPass, newPass, confirmNewPass): - /api.myservice.com/v1/user/account/change-password
    @PutMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetail userDetailsCustom, @RequestBody FormChangePasswordRequest formChangePasswordRequest) throws DataExistException {
        userService.changePassword(userDetailsCustom, formChangePasswordRequest);
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Mật khẩu đã được thay đổi thành công!", HttpStatus.OK), HttpStatus.OK);
    }

    //  lấy ra danh sách lịch sử mua hàng - /api.myservice.com/v1/user/history
    @GetMapping("/history")
    public ResponseEntity<?> getUserPurchaseHistory(@AuthenticationPrincipal CustomUserDetail userDetailsCustom, Pageable pageable) throws DataExistException {
        List<OrderResponse> orderResponseList = orderService.findByUser(userDetailsCustom, pageable);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderResponseList, HttpStatus.OK), HttpStatus.OK);
    }

    //  lấy ra  chi tiết đơn hàng theo số serial - /api.myservice.com/v1/user/history/{serialNumber}
    @GetMapping("/history/{serialNumber}")
    public ResponseEntity<?> getOrderDetailsBySerialNumber(@PathVariable String serialNumber, @AuthenticationPrincipal CustomUserDetail userDetailsCustom) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderService.findBySerialNumber(serialNumber, userDetailsCustom), HttpStatus.OK), HttpStatus.OK);
    }

    //  lấy ra danh sách lịch sử đơn hàng theo trạng thái đơn hàng - /api.myservice.com/v1/user/history/{orderStatus}
    @GetMapping("/history2/{orderStatus}")
    public ResponseEntity<?> findByUserAndStatusOrderStatusName(@AuthenticationPrincipal CustomUserDetail userDetailsCustom, @PathVariable String orderStatus, Pageable pageable) throws DataExistException {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderService.findByUserAndStatusOrderStatusName(userDetailsCustom, orderStatus, pageable), HttpStatus.OK), HttpStatus.OK);
    }

}

