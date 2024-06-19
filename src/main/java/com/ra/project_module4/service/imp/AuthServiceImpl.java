package com.ra.project_module4.service.imp;

import com.ra.project_module4.model.dto.request.DtoFormLogin;
import com.ra.project_module4.model.dto.request.DtoFormRegister;
import com.ra.project_module4.model.dto.response.JWTResponse;
import com.ra.project_module4.model.entity.Role;
import com.ra.project_module4.model.entity.RoleName;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.RoleRepository;
import com.ra.project_module4.repository.UserRepository;
import com.ra.project_module4.security.jwt.JWTProvider;
import com.ra.project_module4.security.principals.CustomUserDetail;
import com.ra.project_module4.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTProvider jwtProvider;

    @Override
    public boolean register(DtoFormRegister dtoFormRegister) {
        if (userRepository.existsByUsername(dtoFormRegister.getUsername())) {
            return false;
        }
        User user = User.builder()
                .username(dtoFormRegister.getUsername())
                .password(passwordEncoder.encode(dtoFormRegister.getPassword()))
                .email(dtoFormRegister.getEmail())
                .fullname(dtoFormRegister.getFullName())
                .address(dtoFormRegister.getAddress())
                .phone(dtoFormRegister.getPhone())
                .status(true)
                .build();
        List<Role> roles = new ArrayList<>();
        if (dtoFormRegister.getRoles() != null && !dtoFormRegister.getRoles().isEmpty()) {
            dtoFormRegister.getRoles().forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        roles.add(roleRepository.findRoleByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("role not found")));
                        break;
                    case "ROLE_USER":
                        roles.add(roleRepository.findRoleByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        break;
                    case "ROLE_MANAGER":
                        roles.add(roleRepository.findRoleByRoleName(RoleName.ROLE_MANAGER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        break;
                }
            });
        } else {
            roles.add(roleRepository.findRoleByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
        }
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    @Override
    public JWTResponse login(DtoFormLogin dtoFormLogin) {
        Authentication authentication = null;
        try {
            //check thông tin đăng nhập gồm username và password có đúng hay không?
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dtoFormLogin.getUsername(), dtoFormLogin.getPassword()));
        } catch (AuthenticationException e) {
            log.error("Username or password not valid!");
        }
        //Lấy thông tin tài khoản đã đăng nhập lưu thông authentication
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        //Tạo chuỗi token từ thông tin tài khoản
        String token = jwtProvider.generateToken(userDetail);

        //Trả về cho người dùng dữ liệu khi login xong (bao gồm cả chuỗi token và các thông tin liên quan tài khoản)
        return JWTResponse.builder()
                .fullName(userDetail.getFullName())
                .address(userDetail.getAddress())
                .email(userDetail.getEmail())
                .status(userDetail.getStatus())
                .authorities(userDetail.getAuthorities())
                .token(token)
                .build();
    }
}
