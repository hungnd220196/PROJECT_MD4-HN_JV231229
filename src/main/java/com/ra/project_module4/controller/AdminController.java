package com.ra.project_module4.controller;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.request.FormCategoryRequest;
import com.ra.project_module4.model.dto.request.FormProductRequest;
import com.ra.project_module4.model.dto.response.ProductResponse;
import com.ra.project_module4.model.dto.response.ResponseDtoSuccess;
import com.ra.project_module4.model.entity.Category;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.service.CategoryService;
import com.ra.project_module4.service.ProductService;
import com.ra.project_module4.service.RoleService;
import com.ra.project_module4.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class AdminController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size,
                                      @RequestParam(defaultValue = "userId") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<User> users = userService.getUsers(page, size, sortField, sortDirection);
        return new ResponseEntity<>(users.getContent(), HttpStatus.OK);
    }

    //lay ve danh sach cac quyen
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        return new ResponseEntity<>(roleService.findAllRole(), HttpStatus.OK);

    }

    //search user theo tên
    @GetMapping("/users/search")
    public ResponseEntity<?> getUsersByName(@RequestParam("name") String name) {
        return new ResponseEntity<>(userService.findByUsername(name), HttpStatus.OK);
    }

    //khoa mo tai khoan nguoi dung
    @PutMapping("users/{userId}")
    public ResponseEntity<?> BlockAndUnblockById(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(userService.blockAndUnlockUser(userId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // API: Lấy về Danh sách Tất cả Sản phẩm (có Sắp xếp và phân trang)
    @GetMapping("/products")
    public ResponseEntity<?> getAllProduct(Pageable pageable) {
        Page<Product> productPage = productService.findAll(pageable);
        List<Product> productList = productPage.getContent();
        return getProductResponseEntity(productList);
    }

    static ResponseEntity<?> getProductResponseEntity(List<Product> productList) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse;
            productResponse = ProductResponse.builder()
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
            productResponseList.add(productResponse);
        }
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponseList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thông tin chi tiết của Sản phẩm theo ID
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getDetailsProductById(@PathVariable Long productId) {
        Product product = productService.findById(productId);
        ProductResponse productResponse;
        productResponse = ProductResponse.builder()
                .sku(product.getSku())
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory().getCategoryName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thêm mới Sản phẩm
    @PostMapping("/products")
    public ResponseEntity<?> addNewProduct(@RequestBody FormProductRequest formProductRequest) throws DataExistException {
        Map<String, Object> map = new HashMap<>();
        Product product = productService.save(formProductRequest);
        map.put("Đã thêm thành công Sản phẩm: ", product);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(map, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // API: Chỉnh sửa thông tin sản phẩm
    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @Valid @RequestBody FormProductRequest formProductRequest) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productService.save(formProductRequest, productId), HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xoá Sản phẩm
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Đã xoá thành công sản phẩm có ID: " + productId, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Lấy về danh sách tất cả danh mục (sắp xếp và phân trang)
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory(Pageable pageable) {
        Page<Category> categoryPage = categoryService.findAll(pageable);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryPage, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Lấy về thông tin danh mục theo id
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getDetailsCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(category, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thêm mới danh mục
    @PostMapping("/categories")
    public ResponseEntity<?> addNewCategory(@RequestBody FormCategoryRequest categoryRequest) throws DataExistException {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryService.save(categoryRequest), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // API: Chỉnh sửa thông tin danh mục
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody FormCategoryRequest categoryRequest) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryService.save(categoryService.save(categoryRequest, categoryId)), HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xóa danh mục
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Đã xoá thành công Danh mục có Id: " + categoryId, HttpStatus.OK), HttpStatus.OK);
    }
}
