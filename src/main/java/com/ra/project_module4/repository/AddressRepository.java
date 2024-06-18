package com.ra.project_module4.repository;

import com.ra.project_module4.model.entity.Address;
import com.ra.project_module4.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
