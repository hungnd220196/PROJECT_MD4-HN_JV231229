package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUser(User user);
}
