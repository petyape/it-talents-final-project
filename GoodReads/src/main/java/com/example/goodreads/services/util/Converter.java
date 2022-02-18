package com.example.goodreads.services.util;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.model.dto.AddressWithoutUserDTO;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.entities.Address;
import com.example.goodreads.model.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Converter {
    @Autowired
    private ModelMapper mapper;

    public void mapToExistingUser(UserWithAddressDTO dto, User user) {
        if (dto == null || user == null) {
            throw new NullPointerException("Empty parameters!");
        }
        if (user.getUserId() != dto.getUserId()) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setGender(dto.getGender());
        user.setUsername(dto.getUsername());
        user.setShowLastName(dto.getShowLastName());
        user.setIsReverseNameOrder(dto.getIsReverseNameOrder());
        user.setGenderViewableBy(dto.getGenderViewableBy());
        user.setLocationViewableBy(dto.getLocationViewableBy());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setWebSite(dto.getWebSite());
        user.setInterests(dto.getInterests());
        user.setBooksPreferences(dto.getBooksPreferences());
        user.setAboutMe(dto.getAboutMe());
        if (user.getAddress().getAddressId() != dto.getAddress().getAddressId()) {
            dto.getAddress().setAddressId(user.getAddress().getAddressId());
        }
        Address newAddress = mapToAddress(dto.getAddress(), user);
        user.setAddress(newAddress);
    }

    public Address mapToAddress(AddressWithoutUserDTO dto, User user) {
        if (dto == null || user == null) {
            throw new NullPointerException("Empty parameters!");
        }
        Address newAddress = mapper.map(dto, Address.class);
        
        return newAddress;
    }
}
