package com.ra.project_module4.service;

import com.ra.project_module4.model.entity.Role;

import java.util.List;

public interface RoleService extends IGenericService<Role,Long> {
    List<Role> findAllRole();
}
