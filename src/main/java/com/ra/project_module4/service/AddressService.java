package com.ra.project_module4.service;

import com.ra.project_module4.model.entity.Address;
import com.ra.project_module4.model.entity.User;

import java.util.List;

public interface AddressService {
   List<Address> findAll();
   Address getAddressByID(Long id);
   Address saveAddress(Address address);
   void deleteAddress(Long id);
}
