package com.ra.project_module4.controller;

import com.ra.project_module4.model.dto.request.DtoFormLogin;
import com.ra.project_module4.model.dto.request.DtoFormRegister;
import com.ra.project_module4.service.AuthService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/auth/sign-up")
    public ResponseEntity<?> register(@RequestBody DtoFormRegister dtoFormRegister) {
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
    public ResponseEntity<?> login(@RequestBody DtoFormLogin dtoFormLogin) {
        return new ResponseEntity<>(authService.login(dtoFormLogin), HttpStatus.OK);
    }
}
