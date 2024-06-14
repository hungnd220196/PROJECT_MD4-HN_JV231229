package com.ra.project_module4.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.project_module4.security.jwt.JWTAuthenticationTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SpringSecurity {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JWTAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Xử lý dạng tấn công cross site request forgecy
        http.csrf(csrf -> csrf.disable())
                //Thiết lập ngoại lệ khi không có quyền truy cập
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler((request, response, accessDenieException) -> {
                    response.setStatus(403);
                    response.setHeader("403", "Forbidden");
                    Map<String, String> map = new HashMap<>();
                    map.put("error", "Forbidden");
                    new ObjectMapper().writeValue(response.getOutputStream(), map);
                }))
                //Xử lý truy cập từ server khác tới
                .cors(Customizer.withDefaults())
                //Thiết lập xác thực các quyền theo các role
                .authorizeHttpRequests(cus -> cus
                        .requestMatchers("/api.myservice.com/v1/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api.myservice.com/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api.myservice.com/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().permitAll())
                //Thiết lập cơ chế xử lý session (STATELESS: không lưu thông tin sau khi request xong)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Thiết lập authen provider với lại cơ chế mã hóa mật khẩu và xác thực bằng user detail service
                .authenticationProvider(authenticationProvider())
                //Thêm filter vào để xử lý
                .addFilterAfter(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
