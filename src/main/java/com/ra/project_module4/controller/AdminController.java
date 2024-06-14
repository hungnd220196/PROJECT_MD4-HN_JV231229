package com.ra.project_module4.controller;

import com.ra.project_module4.model.entity.Role;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.service.RoleService;
import com.ra.project_module4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class AdminController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "2") int size,
                                      @RequestParam(defaultValue = "userId") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<User> users = userService.getUsers(page, size, sortField, sortDirection);
        return new ResponseEntity<>(users.getContent(), HttpStatus.OK);
    }

    //lay ve danh sach cac quyen
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        return new ResponseEntity<>(roleService.findAllRole(), HttpStatus.OK);

    }

    //search user theo tên
    @GetMapping("/users/search")
    public ResponseEntity<?> getUsersByName(@RequestParam("name") String name) {
        return new ResponseEntity<>(userService.findByUsername(name), HttpStatus.OK);
    }

    //khoa mo tai kh nguoi dung
    @PutMapping("users/{userId}")
    public ResponseEntity<?> BlockAndUnblockById(@PathVariable Long userId) {
        return null;
    }
}
