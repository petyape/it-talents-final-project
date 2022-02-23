package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.bookDTO.BookResponseDTO;
import com.example.goodreads.model.dto.userDTO.*;
import com.example.goodreads.model.entities.*;
import com.example.goodreads.model.repository.*;
import com.example.goodreads.services.util.Helper;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PrivacyRepository privacyRepository;
    @Autowired
    private BookshelfRepository bookshelfRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PrivacyService privacyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;

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
        if (!passwordEncoder.matches(password, u.getPassword())) {
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
        if (firstName.trim().length() < 2) {
            throw new BadRequestException("Name is too short!");
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
                .privacy(pr)
                .isAdmin(false).build();
        return userRepository.save(user);
    }

    @Transactional
    public User editProfile(UserWithAddressDTO dto, long loggedUserId) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if (loggedUserId != userId) {
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
    public User editPrivacy(UserWithPrivacyDTO dto, long loggedUserId) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if (loggedUserId != userId) {
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

    public User changePassword(ChangePasswordDTO dto, long loggedUserId) {
        if (dto == null) {
            throw new NullPointerException("No user provided!");
        }
        long userId = dto.getUserId();
        if (loggedUserId != userId) {
            throw new BadRequestException("Wrong user ID provided!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
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
    public String uploadFile(MultipartFile file, long loggedUserId) {
        if (file == null) {
            throw new BadRequestException("There is no photo provided!");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String photoName = System.nanoTime() + "." + extension;
        Files.copy(file.getInputStream(), new File("profile_photos" + File.separator + photoName).toPath());
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> (new NotFoundException("User not found!")));
        user.setPhotoUrl(photoName);
        userRepository.save(user);
        return photoName;
    }

    @Transactional
    public String deleteUser(long loggedUserId) {
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> (new NotFoundException("User not found!")));
        userRepository.deleteById(user.getUserId());
        addressRepository.deleteById(user.getAddress().getAddressId());
        privacyRepository.deleteById(user.getPrivacy().getPrivacyId());
        return "Successfully deleted user with id " + user.getUserId() + ".";
        //TODO delete: READING CHALLENGE ENTITY
        //TODO delete in rest of the tables
    }


    public List<BookResponseDTO> getUserBookshelf(long id, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Bookshelf shelf = bookshelfRepository.findBookshelfByBookshelfId(id);
        List<Book> userBooks = bookRepository.findBooksByUserIdAndBookshelfId(userId, id);
        List<BookResponseDTO> booksPerUserDTO = new ArrayList<>();
        userBooks.forEach(b -> {
            BookResponseDTO dto = mapper.map(b, BookResponseDTO.class);
            booksPerUserDTO.add(dto);
        });
        return booksPerUserDTO;
    }


    public User getUser(long id, long userId) {
        User user = userRepository.findById(id).orElseThrow(() -> (new NotFoundException("User not found!")));
        User loggedUser = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (user.getPrivacy().getViewProfile() == 'n') {
            throw new DeniedPermissionException("Sorry, this user is private!");
        }
        if (user.getPrivacy().getViewProfile() == 'f') {
            if (!user.getFriends().contains(loggedUser)) {
                throw new DeniedPermissionException(("Sorry you are not friends with " + user.getFirstName()));
            }
        }
        GetUserResponseDTO dto = new GetUserResponseDTO();
        if (user.getShowLastName()) {
            dto.setLastName(user.getLastName());
        }
        if (user.getPrivacy().getIsEmailVisible()) {
            dto.setEmail(user.getEmail());
        }
        if (user.getGenderViewableBy() == 'f') {
            if (!user.getFriends().contains(loggedUser)) {
                dto.setGender(user.getGender());
            }
        }
        if (user.getGenderViewableBy() == 'e') {
            dto.setGender(user.getGender());
        }
        if (user.getPrivacy().getCanDisplayReviews()) {
//            List<ReviewResponseDTO> reviews = reviewService.getUserReviews(id);
//            dto.setReviews(reviews);
        }


        //TODO to get person's shelves
        return null;
    }

}