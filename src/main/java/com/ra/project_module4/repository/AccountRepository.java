package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

}
