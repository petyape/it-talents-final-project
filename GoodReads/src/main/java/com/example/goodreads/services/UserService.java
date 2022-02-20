package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.userDTO.ChangePasswordDTO;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.dto.userDTO.UserWithPrivacyDTO;
import com.example.goodreads.model.entities.Address;
import com.example.goodreads.model.entities.Privacy;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.AddressRepository;
import com.example.goodreads.model.repository.PrivacyRepository;
import com.example.goodreads.model.repository.UserRepository;
import com.example.goodreads.services.util.Helper;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;

import static com.example.goodreads.controller.UserController.USER_ID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PrivacyRepository privacyRepository;

    @Autowired
    private PrivacyService privacyService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            throw new BadRequestException("Invalid email address!");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        Helper.validatePassword(password);
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("User with this email already exists!");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("User with this email already exists!");
        }
        Privacy pr = privacyService.createDefaultPrivacy();

        Address address = addressService.createDefaultAddress();

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

    @Transactional
    public User editProfile(UserWithAddressDTO dto, HttpSession session) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if ((Long) session.getAttribute(USER_ID) != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!dto.isValid()) {
            throw new BadRequestException("Wrong account settings provided!");
        }
        if (user.getAddress().getAddressId() != dto.getAddress().getAddressId()) {
            dto.getAddress().setAddressId(user.getAddress().getAddressId());
        }
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
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
        user.setAddress(dto.getAddress());
        addressRepository.save(user.getAddress());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User editPrivacy(UserWithPrivacyDTO dto, HttpSession session) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if ((Long) session.getAttribute(USER_ID) != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (dto.getPrivacy().getPrivacyId() != user.getPrivacy().getPrivacyId() || !dto.isValid()) {
            throw new BadRequestException("Wrong privacy settings provided!");
        }
        user.setPrivacy(dto.getPrivacy());
        privacyRepository.save(user.getPrivacy());
        return userRepository.save(user);
    }

    public User changePassword(ChangePasswordDTO dto, HttpSession session) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if ((Long) session.getAttribute(USER_ID) != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if(!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())){
            throw new DeniedPermissionException("Wrong password provided!");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new DeniedPermissionException("Confirmed password does not match the new password provided!");
        }
        Helper.validatePassword(dto.getNewPassword());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return userRepository.save(user);
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file, HttpServletRequest request) {
        long loggedUser = (long) request.getSession().getAttribute(USER_ID);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String photoName = System.nanoTime() + "." + extension;
        Files.copy(file.getInputStream(), new File("uploads" + File.separator + photoName).toPath());
        User user = userRepository.findById(loggedUser).orElseThrow(() -> (new NotFoundException("User not found!")));
        user.setPhotoUrl(photoName);
        userRepository.save(user);
        return photoName;
    }
}
