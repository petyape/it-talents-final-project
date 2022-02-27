package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.ImageDTO;
import com.example.goodreads.model.dto.bookDTO.BookResponseDTO;
import com.example.goodreads.model.dto.bookDTO.BookshelvesDTO;
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
import java.util.Optional;

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
    private ReadingChallengeRepository readingChallengeRepository;
    @Autowired
    private PrivacyService privacyService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UsersBooksRepository usersBooksRepository;

    private static final String PROFILE_PHOTOS = "profile_photos";

    public UserResponseDTO login(String email, String password) {
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
        return mapper.map(u, UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO register(String email, String password, String firstName, String confirmPassword) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email address is mandatory!");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        if (!Helper.isValidEmail(email)) {
            throw new BadRequestException("Invalid email address!");
        }
        if (!password.equals(confirmPassword)) {
            throw new DeniedPermissionException("Confirmed password does not match the provided password!");
        }
        Helper.validatePassword(password);
        if (firstName == null || firstName.isBlank()) {
            throw new BadRequestException("Name is mandatory!");
        }
        if (firstName.trim().length() < 2) {
            throw new BadRequestException("Name is too short!");
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
        return mapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO editProfile(UserWithAddressDTO dto, long loggedUserId) {
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
        return mapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO editPrivacy(UserWithPrivacyDTO dto, long loggedUserId) {
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
        return mapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    public UserResponseDTO changePassword(ChangePasswordDTO dto, long loggedUserId) {
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
        return mapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    @SneakyThrows
    public ImageDTO uploadFile(MultipartFile file, long loggedUserId) {
        if (file == null) {
            throw new BadRequestException("There is no photo provided!");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String photoName = System.nanoTime() + "." + extension;
        Files.copy(file.getInputStream(), new File("profile_photos" + File.separator + photoName).toPath());
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> (new NotFoundException("User not found!")));
        user.setPhotoUrl(photoName);
        userRepository.save(user);
        return new ImageDTO(user.getPhotoUrl());
    }

    @Transactional
    public UserResponseDTO deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Optional<ReadingChallenge> opt = readingChallengeRepository.findReadingChallengeByUser(user);
        if (opt.isPresent()) {
            readingChallengeRepository.deleteByUser(user);
        }
        userRepository.delete(user);
        addressRepository.deleteById(user.getAddress().getAddressId());
        privacyRepository.deleteById(user.getPrivacy().getPrivacyId());
        return mapper.map(user, UserResponseDTO.class);
    }

    public List<BookResponseDTO> getUserBookshelf(long id, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Bookshelf shelf = bookshelfRepository.findById(id).orElseThrow(() -> (new NotFoundException("Bookshelf not found!")));
        List<Book> userBooks = bookRepository.findBooksByUserIdAndBookshelfId(userId, id);
        List<BookResponseDTO> booksPerUserDTO = new ArrayList<>();
        userBooks.forEach(b -> {
            BookResponseDTO dto = mapper.map(b, BookResponseDTO.class);
            booksPerUserDTO.add(dto);
        });
        return booksPerUserDTO;
    }

    public BookshelvesDTO getBookshelves(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        List<UsersBooks> userBooks = usersBooksRepository.findByUser(user);
        BookshelvesDTO shelves = new BookshelvesDTO();
        for (UsersBooks book : userBooks) {
            BookResponseDTO dto = new BookResponseDTO();
            if (book.getBookshelf().getName().equalsIgnoreCase("Read")) {
                dto.setTitle(book.getBook().getTitle());
                dto.setBookId(book.getBook().getBookId());
                shelves.addReadBook(dto);
            }
            if (book.getBookshelf().getName().equalsIgnoreCase("Want to read")) {
                dto.setTitle(book.getBook().getTitle());
                dto.setBookId(book.getBook().getBookId());
                shelves.addWantToReadBook(dto);
            }
            if(book.getBookshelf().getName().equalsIgnoreCase("Currently reading")){
                dto.setTitle(book.getBook().getTitle());
                dto.setBookId(book.getBook().getBookId());
                shelves.addReadingBook(dto);
            }
        }
        return shelves;
    }

    public GetUserDTO getUser(long userId, long loggedUserId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> (new NotFoundException("User not found!")));
        User loggedUser = userRepository
                .findById(loggedUserId)
                .orElseThrow(() -> (new NotFoundException("User not found!")));

        GetUserDTO dto = mapper.map(user, GetUserDTO.class);
        boolean isFriend = false;

        if (userId != loggedUserId) {
            isFriend = user.getFriends().contains(loggedUser);
            if (Helper.Visibility.getVisibility(user.getPrivacy().getViewProfile()) == Helper.Visibility.NONE) {
                throw new DeniedPermissionException("Private user!");
            }
            if (Helper.Visibility.getVisibility(user.getPrivacy().getViewProfile()) == Helper.Visibility.FRIENDS) {
                if (!isFriend) {
                    throw new DeniedPermissionException(("User can be viewed by friends only!"));
                }
            }
            if (!user.getShowLastName() && !isFriend) {
                dto.setLastName(null);
            }
            if (!user.getPrivacy().getIsEmailVisible() && !isFriend) {
                dto.setEmail(null);
            }
            Helper.Visibility genderVisibility = Helper.Visibility.getVisibility(user.getGenderViewableBy());
            if (genderVisibility == Helper.Visibility.FRIENDS && !isFriend || genderVisibility == Helper.Visibility.NONE) {
                dto.setGender(' ');
            }
        }
        if (user.getPrivacy().getCanDisplayReviews() || isFriend || userId == loggedUserId) {
            dto.setNumberOfReviews(user.getReviews().size());
        }
        dto.setNumberOfRatings(user.getRatings().size());
        if (dto.getNumberOfRatings() == 0) {
            dto.setAverageRatings(0.0);
        } else {
            int sumRatings = user.getRatings().stream().mapToInt(Rating::getRating).sum();
            dto.setAverageRatings(sumRatings * 1.0 / dto.getNumberOfRatings());
        }
        dto.setRead(bookRepository.countBookByBookshelfIdAndUserId(1, dto.getUserId()));
        dto.setCurrentlyReading(bookRepository.countBookByBookshelfIdAndUserId(2, dto.getUserId()));
        dto.setWantToRead(bookRepository.countBookByBookshelfIdAndUserId(3, dto.getUserId()));
        return dto;
    }

    public File getPhoto(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> (new NotFoundException("User not found!")));
        File f = new File(PROFILE_PHOTOS + File.separator + user.getPhotoUrl());
        if (!f.exists()) {
            throw new NotFoundException("Profile photo does not exist");
        }
        return f;
    }

}