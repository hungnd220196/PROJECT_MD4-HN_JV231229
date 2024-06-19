package com.ra.project_module4.controller;

import com.ra.project_module4.model.dto.request.DtoFormLogin;
import com.ra.project_module4.model.dto.request.DtoFormRegister;
import com.ra.project_module4.service.AuthService;
import com.ra.project_module4.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> register(@Valid @RequestBody DtoFormRegister dtoFormRegister) {
        if (userService.existsUsername(dtoFormRegister.getUsername())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Username already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        boolean check = authService.register(dtoFormRegister);
        if (check) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Đã tạo tài khoản thành công.");
            return new ResponseEntity<>(map, HttpStatus.CREATED);
        } else {
            throw new RuntimeException("Có lỗi gì đó xảy ra");
        }

    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<?> login(@Valid @RequestBody DtoFormLogin dtoFormLogin) {
        return new ResponseEntity<>(authService.login(dtoFormLogin), HttpStatus.OK);
    }
}
