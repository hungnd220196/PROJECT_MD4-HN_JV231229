package com.ra.project_module4.service;

import com.ra.project_module4.model.dto.request.AddressRequest;
import com.ra.project_module4.model.entity.Address;
import com.ra.project_module4.model.entity.User;

import java.util.List;

public interface AddressService {
   Address addAddress(AddressRequest addressRequest,User user);
   void deleteAddress(Long addressId);
   List<Address> getAddresses();
   List<String> getAddress(Long addressId);
}
