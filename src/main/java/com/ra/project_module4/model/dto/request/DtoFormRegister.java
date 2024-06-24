package com.ra.project_module4.model.dto.request;

import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.validator.NameExist;
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
    @NameExist(entityClass = User.class, existName = "username",message = "Username already exists")
    private String username;
    @NotBlank(message = "password is empty")
    private String password;
    @NotBlank(message = "fullName is empty")
    private String fullName;
    @NotBlank(message = "address is empty")
    private String address;
    @NotBlank(message = "phone is empty")
    @Pattern(regexp = "^(?:\\+84|0)(3[2-9]|5[6|8|9]|7[0|6|7|8|9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Invalid phone format!")
    @NameExist(entityClass = User.class,existName = "phone",message = "phone already exists")
    private String phone;
    @NameExist(entityClass = User.class,existName = "email",message = "email already exists")
    @NotBlank(message = "email is empty")
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$", message = "Invalid email format!")
    private String email;
    private List<String> roles;
}
