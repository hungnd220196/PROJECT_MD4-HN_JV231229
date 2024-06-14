package com.ra.project_module4.service;

import com.ra.project_module4.model.dto.request.DtoFormLogin;
import com.ra.project_module4.model.dto.request.DtoFormRegister;
import com.ra.project_module4.model.dto.response.JWTResponse;

public interface AuthService {
    boolean register(DtoFormRegister dtoFormRegister);

    JWTResponse login(DtoFormLogin dtoFormLogin);
}
