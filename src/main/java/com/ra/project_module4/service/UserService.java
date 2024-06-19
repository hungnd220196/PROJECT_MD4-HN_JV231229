package com.ra.project_module4.service;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.request.FormChangePasswordRequest;
import com.ra.project_module4.model.dto.request.FormEditUserRequest;
import com.ra.project_module4.model.dto.response.UserDetailResponse;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.security.principals.CustomUserDetail;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    boolean existsUsername(String username);
    User findById(Long id);

    Optional<User> findByUsername(String username);

    Page<User> getUsers(int page, int size, String sortField, String sortDirection);

    Page<User> findByUsernameContaining(String searchName, Integer page, Integer itemPage, String orderBy, String direction);

    User blockAndUnlockUser(Long userId);
    UserDetailResponse getUserDetail(CustomUserDetail userDetailsCustom);

    UserDetailResponse editUserDetail(CustomUserDetail userDetailsCustom, FormEditUserRequest formEditUserRequest);

    void changePassword(CustomUserDetail userDetailsCustom, FormChangePasswordRequest formChangePasswordRequest) throws DataExistException;
}
