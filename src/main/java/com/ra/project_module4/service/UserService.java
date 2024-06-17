package com.ra.project_module4.service;

import com.ra.project_module4.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    User findById(Long id);

    Optional<User> findByUsername(String username);

    Page<User> getUsers(int page, int size, String sortField, String sortDirection);

    Page<User> findByUsernameContaining(String searchName, Integer page, Integer itemPage, String orderBy, String direction);

    User blockAndUnlockUser(Long userId);
}
