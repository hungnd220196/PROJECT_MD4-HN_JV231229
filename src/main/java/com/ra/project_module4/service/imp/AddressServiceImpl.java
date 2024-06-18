package com.ra.project_module4.service.imp;

import com.ra.project_module4.model.entity.Address;
import com.ra.project_module4.repository.AddressRepository;
import com.ra.project_module4.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }


    @Override
    public Address getAddressByID(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new NoSuchElementException("address not found"));

    }
    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
