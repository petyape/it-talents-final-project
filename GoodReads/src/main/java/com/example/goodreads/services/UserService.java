package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.AddressRepository;
import com.example.goodreads.model.repository.UserRepository;
import com.example.goodreads.services.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static com.example.goodreads.controller.UserController.USER_ID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Converter converter;

    @Transactional
    public User editProfile(UserWithAddressDTO dto, HttpSession session) {
        int userId = dto.getUserId();
        if ((Integer)session.getAttribute(USER_ID) != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!dto.isValid()) {
            throw new BadRequestException("Wrong account settings provided!");
        }
        converter.mapToExistingUser(dto, user);
        userRepository.save(user);
        addressRepository.save(user.getAddress());
        return user;
    }

}
