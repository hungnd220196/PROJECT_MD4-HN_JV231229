package com.ra.project_module4.model.dto.request;

import com.ra.project_module4.validator.ConfirmMatchingPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ConfirmMatchingPassword(password = "newPass", confirmPassword = "confirmNewPass")
public class FormChangePasswordRequest {
    @NotBlank(message = "oldPass is empty")
    private String oldPass;
    @NotBlank(message = "oldPass is empty")
    private String newPass;
    @NotBlank(message = "oldPass is empty")
    private String confirmNewPass;
}
