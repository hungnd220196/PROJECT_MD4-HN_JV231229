package com.ra.project_module4.service.imp;

import com.ra.project_module4.exception.DataExistException;
import com.ra.project_module4.model.dto.request.FormChangePasswordRequest;
import com.ra.project_module4.model.dto.request.FormEditUserRequest;
import com.ra.project_module4.model.dto.response.UserDetailResponse;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.UserRepository;
import com.ra.project_module4.security.principals.CustomUserDetail;
import com.ra.project_module4.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Page<User> getUsers(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUsernameContaining(String searchName, Integer page, Integer itemPage, String orderBy, String direction) {
        Pageable pageable = null;

        if (orderBy != null && !orderBy.isEmpty()) {
            // co sap xep
            Sort sort = null;
            switch (direction) {
                case "ASC":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "DESC":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, itemPage, sort);
        } else {
            //khong sap xep
            pageable = PageRequest.of(page, itemPage);
        }

        //xu ly ve tim kiem
        if (searchName != null && !searchName.isEmpty()) {
            //co tim kiem
            return userRepository.findByUsernameContaining(searchName, pageable);
        } else {
            //khong tim kiem
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public User blockAndUnlockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setStatus(!user.getStatus());
        return userRepository.save(user);
    }

    @Override
    public UserDetailResponse getUserDetail(CustomUserDetail userDetailsCustom) {
        User user = findById(userDetailsCustom.getId());

        List<? extends GrantedAuthority> authorityList = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList();

        return UserDetailResponse.builder()
                .roleSet(authorityList)
                .username(user.getUsername())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .address(user.getAddress())
                .email(user.getEmail())
                .fullName(user.getFullname())
                .build();
    }

    @Override
    public User editUserDetail(CustomUserDetail userDetailsCustom, FormEditUserRequest formEditUserRequest) {
        log.info("Editing user details for user ID: {}", userDetailsCustom.getId());

        User user = findById(userDetailsCustom.getId());
        if (formEditUserRequest.getEmail() != null) user.setEmail(formEditUserRequest.getEmail());
        if (formEditUserRequest.getAddress() != null) user.setAddress(formEditUserRequest.getAddress());
        if (formEditUserRequest.getFullName() != null) user.setFullname(formEditUserRequest.getFullName());
        if (formEditUserRequest.getPhone() != null) user.setPhone(formEditUserRequest.getPhone());
        user.setUpdatedAt(new Date());

        userRepository.save(user);

        log.info("User details updated successfully for user ID: {}", user.getUserId());

        return user;
    }

    @Override
    public void changePassword(CustomUserDetail userDetailsCustom, FormChangePasswordRequest formChangePasswordRequest) throws DataExistException {
        User user = findById(userDetailsCustom.getId());

        // Xác thực thông qua username và password.
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), formChangePasswordRequest.getOldPass()));
        } catch (AuthenticationException e) {
            throw new DataExistException("Mật khẩu cũ không chính xác.", "Lỗi");
        }
        CustomUserDetail detailsCustom = (CustomUserDetail) authentication.getPrincipal();

        if (isValidPassword(formChangePasswordRequest.getNewPass())) {
            throw new DataExistException("Mật khẩu không đúng định dạng. Phải lớn hơn 8 kí tự", "Lỗi");
        }

        if (!formChangePasswordRequest.getNewPass().equals(formChangePasswordRequest.getConfirmNewPass())) {
            throw new DataExistException("Nhập lại mật khẩu không đúng!", "Lỗi");
        }

        user.setPassword(passwordEncoder.encode(formChangePasswordRequest.getNewPass()));
        userRepository.save(user);
    }

    @Override
    public List<User> findByUsernameContainingIgnoreCase(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}
