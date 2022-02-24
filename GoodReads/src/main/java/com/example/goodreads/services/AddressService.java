package com.example.goodreads.services;

import com.example.goodreads.model.entities.Address;
import com.example.goodreads.model.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createDefaultAddress() {
        Address address = Address.builder().build();
        return addressRepository.save(address);
    }

}
