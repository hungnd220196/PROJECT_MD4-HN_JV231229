package com.ra.project_module4.service.imp;

import com.ra.project_module4.model.entity.Role;
import com.ra.project_module4.repository.RoleRepository;
import com.ra.project_module4.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public Page<Role> findAll() {
        return null;
    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }


    @Override
    public Role save(Role entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
