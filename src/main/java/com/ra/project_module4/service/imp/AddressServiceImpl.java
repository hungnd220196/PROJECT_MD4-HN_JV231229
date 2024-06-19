package com.ra.project_module4.service.imp;

import com.ra.project_module4.model.dto.request.AddressRequest;
import com.ra.project_module4.model.entity.Address;
import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.AddressRepository;
import com.ra.project_module4.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address addAddress(AddressRequest addressRequest, User user) {
        Address address = new Address();
        address.setPhone(addressRequest.getPhone());
        address.setReceiveName(addressRequest.getReceiveName());
        address.setFullAddress(addressRequest.getFullAddress());
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public List<Address> getAddresses() {

        return addressRepository.findAll();
    }

    @Override
    public List<String> getAddress(Long addressId) {

        Optional<Address> addressOptional = addressRepository.findById(addressId);

        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            return List.of(
                    "Address ID: " + address.getAddressId(),
                    "Full Address: " + address.getFullAddress(),
                    "Phone: " + address.getPhone(),
                    "Receive Name: " + address.getReceiveName()
            );
        } else {

            return List.of("Address not found");
        }

    }

}
