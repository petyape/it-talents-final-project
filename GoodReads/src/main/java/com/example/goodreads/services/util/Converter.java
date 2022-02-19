package com.example.goodreads.services.util;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.entities.Privacy;
import com.example.goodreads.model.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Converter {
    @Autowired
    private ModelMapper mapper;

    public void mapToUser(UserWithAddressDTO dto, User user) {
        if (dto == null || user == null) {
            throw new NullPointerException("Empty parameters!");
        }
        if (user.getUserId() != dto.getUserId()) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        String photoUrl = user.getPhotoUrl();
        String password = user.getPassword();
        Privacy privacy = user.getPrivacy();
        if (user.getAddress().getAddressId() != dto.getAddress().getAddressId()) {
            dto.getAddress().setAddressId(user.getAddress().getAddressId());
        }
        user = mapper.map(dto, User.class);
        user.setPhotoUrl(photoUrl);
        user.setPassword(password);
        user.setPrivacy(privacy);
    }
}
