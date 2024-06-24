package com.ra.project_module4.model.dto.request;

import com.ra.project_module4.model.entity.Category;
import com.ra.project_module4.validator.NameExist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FormCategoryRequest {
    @NameExist(entityClass = Category.class, existName = "categoryName", message = "Category already exists")
    private String categoryName;

    private String description;

}
