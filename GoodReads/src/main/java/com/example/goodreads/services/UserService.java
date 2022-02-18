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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.regex.Pattern;

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


    @Transactional
    public User editProfile(UserWithAddressDTO dto, HttpSession session) {
        int userId = dto.getUserId();
        if ((Integer) session.getAttribute(USER_ID) != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById((long) userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!dto.isValid()) {
            throw new BadRequestException("Wrong account settings provided!");
        }
        converter.mapToExistingUser(dto, user);
        userRepository.save(user);
        addressRepository.save(user.getAddress());
        return user;
    }


    public User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is mandatory!");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        User u = userRepository.findByEmailAndPassword(email, password);
        if (u == null) {
            throw new UnauthorizedException("Wrong credentials!");
        }
        if(!passwordEncoder.matches(password, u.getPassword())){
            throw new UnauthorizedException("Wrong credentials!");
        }
        return u;
    }


    public User register(String email, String password, String firstName) {
        String regexPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email address is mandatory!");
        }
        if (!Pattern.compile(regexPattern).matcher(email).matches()) {
            System.out.println("Invalid email address!");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        if (!password.matches("(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) {
            throw new BadRequestException("The password must have at least one lower case letter, " +
                    "at least one upper case letter, " +
                    "at least one number, " +
                    "at least one special char " +
                    "and the length should be at least with 8 characters.");
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
                .viewProfile(Privacy.Visibility.FRIENDS.symbol)
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
                .genderViewableBy(User.Visibility.NONE.symbol)
                .locationViewableBy(User.Visibility.NONE.symbol)
                .address(address)
                .privacy(pr).build();
        return userRepository.save(user);
    }
}
