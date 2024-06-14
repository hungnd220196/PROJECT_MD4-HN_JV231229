package com.ra.project_module4.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    @Pattern(regexp = "^(?=.{6,100}$)[a-zA-Z0-9]+$", message = "Username must be between 6 and 100 characters and contain no special characters.")
    private String username;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$", message = "Invalid email format!")
    private String email;

    @Column(nullable = false, length = 100)
    private String fullname;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Column(nullable = false, unique = true, length = 15)
    @Pattern(regexp = "^(?:\\+84|0)(3[2-9]|5[6|8|9]|7[0|6|7|8|9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Invalid phone format!")
    private String phone;

    @Column(nullable = false)
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles;

}
