package com.ra.project_module4.service.imp;

import com.ra.project_module4.model.dto.response.WishListResponse;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.model.entity.WishList;
import com.ra.project_module4.repository.ProductRepository;
import com.ra.project_module4.repository.UserRepository;
import com.ra.project_module4.repository.WishListRepository;
import com.ra.project_module4.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public WishListResponse addProductToWishlist(Long productId, Long userId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            WishList wishList = new WishList();
            wishList.setProduct(product);
            wishList.setUser(userRepository.findById(userId).get());
            WishList savedWishList = wishListRepository.save(wishList);

            return WishListResponse.builder()
                    .productId(savedWishList.getProduct().getProductId())
                    .productName(savedWishList.getProduct().getProductName())
                    .productDescription(savedWishList.getProduct().getDescription())
                    .productPrice(savedWishList.getProduct().getUnitPrice())
                    .productCategory(savedWishList.getProduct().getCategory().getCategoryName())
                    .build();
        }
        return null;
    }

    @Override
    public List<WishListResponse> getWishList(User currentUser) {
        List<WishList> wishLists = wishListRepository.findAllByUser(currentUser);
        return wishLists.stream()
                .map(wishList -> WishListResponse.builder()
                        .productId(wishList.getProduct().getProductId())
                        .productName(wishList.getProduct().getProductName())
                        .productDescription(wishList.getProduct().getDescription())
                        .productPrice(wishList.getProduct().getUnitPrice())
                        .productCategory(wishList.getProduct().getCategory().getCategoryName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public WishListResponse deleteProductFromWishlist(Long wishListId) {
        Optional<WishList> wishListOptional = wishListRepository.findById(wishListId);
        if (wishListOptional.isPresent()) {
            WishList wishList = wishListOptional.get();
            wishListRepository.deleteById(wishListId);

            return WishListResponse.builder()
                    .productId(wishList.getProduct().getProductId())
                    .productName(wishList.getProduct().getProductName())
                    .productDescription(wishList.getProduct().getDescription())
                    .productPrice(wishList.getProduct().getUnitPrice())
                    .productCategory(wishList.getProduct().getCategory().getCategoryName())
                    .build();
        }
        return null;
    }
}


