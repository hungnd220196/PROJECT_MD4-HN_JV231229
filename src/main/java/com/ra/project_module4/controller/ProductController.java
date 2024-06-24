package com.ra.project_module4.controller;

import com.ra.project_module4.model.dto.PageDTO;
import com.ra.project_module4.model.dto.response.ProductResponse;
import com.ra.project_module4.model.dto.response.ResponseDtoSuccess;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.service.CategoryService;
import com.ra.project_module4.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api.myservice.com/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    // API: Chức năng Tìm kiếm Sản phẩm theo tên.
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "search") String search) {
        List<Product> productList = productService.findByNameOrDescriptionContaining(search);
        if (productList.isEmpty()) {
            return new ResponseEntity<>("Không tìm thấy Sản phẩm có tên: " + search, HttpStatus.NOT_FOUND);
        }
        return getResponseEntity(productList);
    }

    // API: Danh sách sản phẩm được bán(có phân trang và sắp xếp)
    @GetMapping
    public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "2") int size,
                                           @RequestParam(defaultValue = "productId") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageDTO<ProductResponse> productResponsePageDTO = productService.getAllProductRolePermitAll(pageable);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponsePageDTO, HttpStatus.OK), HttpStatus.OK);
    }


    // API: Danh sách Sản phẩm mới: Lấy ra 10 Sản phẩm được thêm gần đây nhất
    @GetMapping("/new-products")
    public ResponseEntity<?> getNewProducts() {
        List<Product> productList = productService.findFirst10ByOrderByCreatedAtDesc();
        return getResponseEntity(productList);
    }


    // API: Danh sách Sản phẩm theo Danh Mục
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> productList = productService.findByCategory(categoryService.findById(categoryId));
        return getResponseEntity(productList);
    }

    // API: Chi tiết Sản phẩm theo ID
    @GetMapping("/{productId}")
    public ResponseEntity<?> getDetailsProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductDetailsById(productId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // Chuyển đổi đối tượng Product
    private ResponseEntity<?> getResponseEntity(List<Product> productList) {
        List<ProductResponse> productResponses = productList.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponses, HttpStatus.OK), HttpStatus.OK);
    }

    private ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stockQuantity(product.getStockQuantity())
                .image(product.getImage())
                .category(product.getCategory().getCategoryName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
