package com.ra.project_module4.controller;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.PageDTO;
import com.ra.project_module4.model.dto.request.FormCategoryRequest;
import com.ra.project_module4.model.dto.request.FormChangeOrderStatus;
import com.ra.project_module4.model.dto.request.FormProductRequest;
import com.ra.project_module4.model.dto.response.OrderResponseRoleAdmin;
import com.ra.project_module4.model.dto.response.ProductResponse;
import com.ra.project_module4.model.dto.response.ResponseDtoSuccess;
import com.ra.project_module4.model.entity.Category;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private OrderService orderService;

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
        return new ResponseEntity<>(userService.findByUsernameContainingIgnoreCase(name), HttpStatus.OK);
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
    public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "2") int size,
                                           @RequestParam(defaultValue = "productId") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageDTO<ProductResponse> productResponsePageDTO = productService.getAllProductRolePermitAll(pageable);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponsePageDTO, HttpStatus.OK), HttpStatus.OK);
    }


    // API: Thông tin chi tiết của Sản phẩm theo ID
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getDetailsProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductDetailsById(productId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thêm mới Sản phẩm
    @PostMapping("/products")
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody FormProductRequest formProductRequest) throws DataExistException {
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
    public ResponseEntity<?> getAllCategory(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "2") int size,
                                            @RequestParam(defaultValue = "categoryId") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
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
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody FormCategoryRequest categoryRequest) throws DataExistException {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryService.save(categoryRequest, categoryId), HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xóa danh mục
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // API: Danh sách tất cả đơn hàng: - /api.myservice.com/v1/admin/orders
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrder(Pageable pageable) {
        Page<OrderResponseRoleAdmin> orderPage = orderService.getAllOrderRoleAdmin(pageable);
        List<OrderResponseRoleAdmin> orderList = orderPage.getContent();
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Danh sách đơn hàng theo trạng thái - /api.myservice.com/v1/admin/orders/{orderStatus}
    @GetMapping("/orders/{orderStatus}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String orderStatus, Pageable pageable) throws DataExistException {
        Page<OrderResponseRoleAdmin> orderResponseRoleAdmins = orderService.findByStatusOrderStatusName(orderStatus, pageable);
        List<OrderResponseRoleAdmin> orderList = orderResponseRoleAdmins.getContent();
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Chi tiết đơn hàng - /api.myservice.com/v1/admin/orders/{orderId}
    @GetMapping("/orders2/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        OrderResponseRoleAdmin orderResponseRoleAdmin = orderService.getOrderById(orderId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderResponseRoleAdmin, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Cập nhật trạng thái đơn hàng (payload : orderStatus) - /api.myservice.com/v1/admin/orders/{orderId}/status
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @RequestBody FormChangeOrderStatus formChangeOrderStatus) {
        OrderResponseRoleAdmin orderResponseRoleAdmin = orderService.updateOrderStatusById(orderId, formChangeOrderStatus.getOrderStatusName());
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderResponseRoleAdmin, HttpStatus.OK), HttpStatus.OK);
    }
}
