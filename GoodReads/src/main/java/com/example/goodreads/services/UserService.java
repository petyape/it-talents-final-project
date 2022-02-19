package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.entities.Address;
import com.example.goodreads.model.entities.Privacy;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.AddressRepository;
import com.example.goodreads.model.repository.PrivacyRepository;
import com.example.goodreads.model.repository.UserRepository;
import com.example.goodreads.services.util.Converter;
import com.example.goodreads.services.util.Helper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.sql.SQLException;

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
    private PrivacyRepository privacyRepository;

    @Autowired
    private Converter converter;

    public User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is mandatory!");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        User u = userRepository.findByEmail(email);
        if (u == null) {
            throw new UnauthorizedException("Wrong credentials!");
        }
        if(!passwordEncoder.matches(password, u.getPassword())){
            throw new UnauthorizedException("Wrong credentials!");
        }
        return u;
    }

    @Transactional
    public User register(String email, String password, String firstName) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email address is mandatory!");
        }
        if (!Helper.isValidEmail(email)) {
            System.out.println("Invalid email address!");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        if (!Helper.isValidPassword(password)) {
            throw new BadRequestException("Password must contain at least one lower case letter, " +
                    "one upper case letter, one number, one special character " +
                    "and should be at least 8 characters long.");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("User with this email already exists!");
        }

        Privacy pr = Privacy.builder()
                .canDisplayReviews(true)
                .allowSearchByEmail(true)
                .canNonFriendsComment(true)
                .canNonFriendsFollow(true)
                .challengeQuestion("")
                .isEmailVisible(true)
                .privateMessages(true)
                .promptToRecommendBooks(true)
                .viewProfile(Helper.Visibility.FRIENDS.symbol)
                .build();
        pr = privacyRepository.save(pr);

        Address address = Address.builder().build();
        address = addressRepository.save(address);

        User user = User.builder()
                .firstName(firstName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .showLastName(true)
                .isReverseNameOrder(false)
                .gender(Helper.Visibility.NONE.symbol)
                .genderViewableBy(Helper.Visibility.NONE.symbol)
                .locationViewableBy(Helper.Visibility.NONE.symbol)
                .address(address)
                .privacy(pr).build();
        return userRepository.save(user);
    }

//    @Transactional(rollbackOn = SQLException.class)
    @Transactional
    public User editProfile(UserWithAddressDTO dto, HttpSession session) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if ((Long) session.getAttribute(USER_ID) != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById( userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!dto.isValid()) {
            throw new BadRequestException("Wrong account settings provided!");
        }
        user = converter.mapToUser(dto, user);
        addressRepository.save(user.getAddress());
        userRepository.save(user);
        return user;
    }


}
