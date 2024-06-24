package com.ra.project_module4.service.imp;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.PageDTO;
import com.ra.project_module4.model.dto.request.FormProductRequest;
import com.ra.project_module4.model.dto.response.ProductResponse;
import com.ra.project_module4.model.entity.Category;
import com.ra.project_module4.model.entity.Product;
import com.ra.project_module4.repository.CategoryRepository;
import com.ra.project_module4.repository.ProductRepository;
import com.ra.project_module4.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new NoSuchElementException("Message: Không tồn tại Id"));
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm!"));
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findByNameOrDescriptionContaining(String searchTerm) {
        return productRepository.findByNameOrDescriptionContaining(searchTerm);
    }


    @Override
    public List<Product> findFirst10ByOrderByCreatedAtDesc() {
        return productRepository.findFirst10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product save(FormProductRequest productRequest) throws DataExistException {
        if (productRepository.existsByProductName(productRequest.getProductName())) {
            throw new DataExistException("Tên sản phẩm đã tồn tại!", "productName");
        }
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .description(productRequest.getDescription())
                .unitPrice(productRequest.getUnitPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .build();
        Optional<Category> categoryOptional = categoryRepository.findById(productRequest.getCategory());
        Category category = categoryOptional.orElseThrow(() -> new NoSuchElementException("Message: Không tồn tại Id"));
        product.setCategory(category);
        product.setSku(UUID.randomUUID().toString());
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public Product save(FormProductRequest formProductRequest, Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        product.setProductName(formProductRequest.getProductName());
        product.setDescription(formProductRequest.getDescription());
        product.setUnitPrice(formProductRequest.getUnitPrice());
        product.setStockQuantity(formProductRequest.getStockQuantity());
        product.setCategory(categoryRepository.findById(formProductRequest.getCategory()).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id")));
        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }

    // Phương thức lấy về DTO danh sách Tất cả sản phẩm có phân trang và sắp xếp.
    @Override
    public PageDTO<ProductResponse> getAllProductRolePermitAll(Pageable pageable) {
        Page<Product> productPage = findAll(pageable);
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productPage) {
            ProductResponse productResponse = ProductResponse.builder()
                    .productId(product.getProductId())
                    .sku(product.getSku())
                    .productName(product.getProductName())
                    .category(product.getCategory().getCategoryName())
                    .updatedAt(product.getUpdatedAt())
                    .createdAt(product.getCreatedAt())
                    .image(product.getImage())
                    .stockQuantity(product.getStockQuantity())
                    .unitPrice(product.getUnitPrice())
                    .description(product.getDescription())
                    .build();

            productResponseList.add(productResponse);
        }
        return new PageDTO<>(new PageImpl<>(productResponseList, pageable, productPage.getTotalElements()));
    }

    @Override
    public List<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> productList = productPage.getContent();
        return convertToProductResponseList(productList);
    }

    private List<ProductResponse> convertToProductResponseList(List<Product> productList) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponse productResponse = ProductResponse.builder()
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
        return productResponseList;
    }

    @Override
    public ProductResponse getProductDetailsById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + productId));

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