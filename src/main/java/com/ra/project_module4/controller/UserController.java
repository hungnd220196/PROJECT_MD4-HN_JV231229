package com.ra.project_module4.controller;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.request.*;
import com.ra.project_module4.model.dto.response.*;
import com.ra.project_module4.model.entity.Address;
import com.ra.project_module4.model.entity.ShoppingCart;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.security.principals.CustomUserDetail;
import com.ra.project_module4.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private WishListService wishListService;

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


    //  Cập nhật thông tin người dùng - /api.myservice.com/v1/user/account
    @PutMapping("/account")
    public ResponseEntity<?> updateUserDetail(@Valid @AuthenticationPrincipal CustomUserDetail userDetailsCustom, @ModelAttribute FormEditUserRequest formEditUserRequest)  {
        // Cập nhật thông tin người dùng
        return new ResponseEntity<>(new ResponseDtoSuccess<>(userService.editUserDetail(userDetailsCustom, formEditUserRequest), HttpStatus.OK), HttpStatus.OK);
    }


    @PostMapping("/account/addresses")
    public ResponseEntity<?> addAddress(@RequestBody AddressRequest addressRequest, @AuthenticationPrincipal CustomUserDetail customUserDetail) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(addressService.addAddress(addressRequest, userService.findById(customUserDetail.getId())), HttpStatus.OK), HttpStatus.OK);

    }

    @DeleteMapping("/account/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/account/addresses")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal CustomUserDetail customUserDetail) {

        return new ResponseEntity<>(addressService.getAddresses(), HttpStatus.OK);
    }


    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<List<String>> getAddress(@PathVariable Long addressId) {
        List<String> addressDetails = addressService.getAddress(addressId);
        return ResponseEntity.ok(addressDetails);
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

    @PutMapping("/history/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderService.cancelOrder(orderId), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/wish-list")
    public ResponseEntity<WishListResponse> addProductToWishList(@RequestParam Long productId, @AuthenticationPrincipal CustomUserDetail customUserDetail) {

        return new ResponseEntity<>(wishListService.addProductToWishlist(productId, customUserDetail.getId()), HttpStatus.OK);
    }


    @GetMapping("/wish-list")
    public ResponseEntity<List<WishListResponse>> getWishList(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        List<WishListResponse> wishListResponses = wishListService.getWishList(userService.findById(customUserDetail.getId()));
        return ResponseEntity.ok(wishListResponses);
    }

    @DeleteMapping("/{wishListId}")
    public ResponseEntity<WishListResponse> deleteProductFromWishlist(@PathVariable Long wishListId) {
        WishListResponse wishListResponse = wishListService.deleteProductFromWishlist(wishListId);
        if (wishListResponse != null) {
            return ResponseEntity.ok(wishListResponse);
        }
        return ResponseEntity.notFound().build();
    }

}

