package com.ra.project_module4.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoFormRegister {
    @NotBlank(message = "user name is empty")
    private String username;
    @NotBlank(message = "password is empty")
    private String password;
    @NotBlank(message = "fullName is empty")
    private String fullName;
    @NotBlank(message = "address is empty")
    private String address;
    @NotBlank(message = "phone is empty")
    private String phone;
    @NotBlank(message = "email is empty")
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$", message = "Invalid email format!")
    private String email;
    private List<String> roles;
}
