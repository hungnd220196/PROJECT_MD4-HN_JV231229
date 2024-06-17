package com.ra.project_module4.service;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.request.FormCategoryRequest;
import com.ra.project_module4.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category findById(Long id);

    Page<Category> findAll(Pageable pageable);

    Category save(Category entity);

    void deleteById(Long id);

    boolean existsByCategoryName(String categoryName);

    Category save(FormCategoryRequest formCategoryRequest, Long categoryID);

    Category save(FormCategoryRequest categoryRequest) throws DataExistException;
}
