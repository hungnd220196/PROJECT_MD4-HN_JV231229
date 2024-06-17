package com.ra.project_module4.service;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.PageDTO;
import com.ra.project_module4.model.dto.request.FormProductRequest;
import com.ra.project_module4.model.dto.response.ProductResponse;
import com.ra.project_module4.model.entity.Category;
import com.ra.project_module4.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product findById(Long id);

    Page<Product> findAll(Pageable pageable);

    Product save(Product entity);

    void deleteById(Long id);

    List<Product> findByProductNameContaining(String proName);

    List<Product> findFirst10ByOrderByCreatedAtDesc();

    List<Product> findByCategory(Category category);

    Product save(FormProductRequest productRequest) throws DataExistException;

    Product save(FormProductRequest formProductRequest, Long productId);

     PageDTO<ProductResponse> getAllProductRolePermitAll(Pageable pageable);
}
