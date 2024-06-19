package com.ra.project_module4.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoFormLogin {
    @NotBlank(message = "username is empty")
    private String username;
    @NotBlank(message = "password is empty")
    private String password;
}

