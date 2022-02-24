package com.example.goodreads.controller;

import com.example.goodreads.model.dto.bookDTO.BookResponseDTO;
import com.example.goodreads.model.dto.userDTO.*;
import com.example.goodreads.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginUserDTO user,
                                                 HttpSession session, HttpServletRequest request) {
        String email = user.getEmail();
        String pass = user.getPassword();
        UserResponseDTO u = userService.login(email, pass);
        session.setAttribute(USER_ID, u.getId());
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(LOGGED, true);
        return ResponseEntity.ok(u);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterUserDTO user,
                                                    HttpSession session, HttpServletRequest request) {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        UserResponseDTO u = userService.register(email, password, firstName);
        session.setAttribute(USER_ID, u.getId());
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        return ResponseEntity.ok(u);
    }

    @PutMapping("/user/edit/profile")
    public ResponseEntity<UserResponseDTO> editProfile(@RequestBody UserWithAddressDTO userEdited,
                                                       HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        UserResponseDTO dto = userService.editProfile(userEdited, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/privacy")
    public ResponseEntity<UserResponseDTO> editPrivacy(@RequestBody UserWithPrivacyDTO userEdited,
                                                       HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        UserResponseDTO dto = userService.editPrivacy(userEdited, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/password")
    public ResponseEntity<UserResponseDTO> changePassword(@RequestBody ChangePasswordDTO newPasswordUser,
                                                          HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        UserResponseDTO dto = userService.changePassword(newPasswordUser, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/photo")
    public String uploadPhoto(@RequestParam(name = "file") MultipartFile file,
                              HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        return userService.uploadFile(file, (long) session.getAttribute(USER_ID));
    }

    @PutMapping("/user/sign_out")
    public ResponseEntity<UserResponseDTO> logout(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        session.invalidate();
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/user/destroy")
    public ResponseEntity<String> deleteAccount(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        String msg = userService.deleteUser((long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/user/bookshelves/{id}")
    public ResponseEntity<List<BookResponseDTO>> getUserBookshelf(@PathVariable long id,
                                                                  HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        long userId = (long) session.getAttribute(USER_ID);
        List<BookResponseDTO> dto = userService.getUserBookshelf(id, userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("user/show/{id}")
    public ResponseEntity<GetUserDTO> getUser(@PathVariable long id, HttpSession session) {
        validateSession(session);
        GetUserDTO userDTO = userService.getUser(id, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(userDTO);
    }

    @SneakyThrows
    @GetMapping("/user/photo/{id}")
    public void getPhoto(@PathVariable long id, HttpSession session, HttpServletResponse response) {
        validateSession(session);
        File photo = userService.getPhoto(id);
        Files.copy(photo.toPath(), response.getOutputStream());
    }

}
