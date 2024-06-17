package com.ra.project_module4.controller;

import com.ra.project_module4.model.dto.request.FormAddToCartRequest;
import com.ra.project_module4.model.dto.request.FormChangeQuantityCartItem;
import com.ra.project_module4.model.dto.response.ResponseDtoSuccess;
import com.ra.project_module4.model.dto.response.ShoppingCartResponse;
import com.ra.project_module4.model.entity.ShoppingCart;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.security.principals.CustomUserDetail;
import com.ra.project_module4.service.ShoppingCartService;
import com.ra.project_module4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> changeQuantityCart(@AuthenticationPrincipal CustomUserDetail customUserDetail, @PathVariable Long cartItemId,@RequestBody FormChangeQuantityCartItem formChangeQuantityCartItem) {
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

}

