package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}
