package com.ra.project_module4.service;

import com.ra.project_module4.model.dto.response.WishListResponse;
import com.ra.project_module4.model.entity.User;

import java.util.List;

public interface WishListService {
    WishListResponse addProductToWishlist(Long productId, Long userId) ;

    List<WishListResponse> getWishList(User currentUser); ;

    WishListResponse deleteProductFromWishlist(Long wishListId) ;
}
