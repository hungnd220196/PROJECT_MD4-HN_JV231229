package com.ra.project_module4.model.dto.response;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserDetailResponse {
    private String username;

    private String email;

    private String fullName;

    private String avatar;

    private String phone;

    private String address;

    private Date createdAt;

    private Date updatedAt;

    private Collection<? extends GrantedAuthority> roleSet;
}
