package com.ra.project_module4.security.principals;

import com.ra.project_module4.exception.UserBlockException;
import com.ra.project_module4.model.entity.Role;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new NoSuchElementException("user not found "));
        return CustomUserDetail.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullname())
                .address(user.getAddress())
                .email(user.getEmail())
                .status(user.getStatus())
                .authorities(mapToAuthorities(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList();

    }
}
