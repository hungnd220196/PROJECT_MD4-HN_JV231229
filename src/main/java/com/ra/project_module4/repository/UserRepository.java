package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    @Query("SELECT u from User u where u.username like concat('%',:username,'%') ")
    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    Page<User> findByUsernameContaining(String username, Pageable pageable);
    boolean existsByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);

}
