package com.example.goodreads.controller;

import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.userDTO.*;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginUserDTO user, HttpSession session, HttpServletRequest request) {
        String email = user.getEmail();
        String pass = user.getPassword();
        User u = userService.login(email, pass);
        session.setAttribute(USER_ID, u.getUserId());
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(LOGGED, true);
        UserResponseDTO dto = mapper.map(u, UserResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterUserDTO user, HttpSession session, HttpServletRequest request) {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        User u = userService.register(email, password, firstName);
        session.setAttribute(USER_ID, u.getUserId());
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        UserResponseDTO dto = mapper.map(u, UserResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/profile")
    public ResponseEntity<UserResponseDTO> editProfile(@RequestBody UserWithAddressDTO userEdited, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        User u = userService.editProfile(userEdited, (long)session.getAttribute(USER_ID));
        UserResponseDTO dto = mapper.map(u, UserResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/privacy")
    public ResponseEntity<UserResponseDTO> editPrivacy(@RequestBody UserWithPrivacyDTO userEdited, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        User u = userService.editPrivacy(userEdited, (long)session.getAttribute(USER_ID));
        UserResponseDTO dto = mapper.map(u, UserResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/password")
    public ResponseEntity<UserResponseDTO> changePassword(@RequestBody ChangePasswordDTO newPasswordUser, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        User u = userService.changePassword(newPasswordUser, (long)session.getAttribute(USER_ID));
        UserResponseDTO dto = mapper.map(u, UserResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/user/edit/photo")
    public String uploadPhoto(@RequestParam(name = "file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        return userService.uploadFile(file, (long)session.getAttribute(USER_ID));
    }

    @PutMapping("/user/sign_out")
    public ResponseEntity<UserResponseDTO> logout(HttpSession session, HttpServletRequest request){
        validateSession(session, request);
        session.invalidate();
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/user/destroy")
    public ResponseEntity<String>  deleteAccount(HttpSession session, HttpServletRequest request){
        validateSession(session, request);
        String msg = userService.deleteUser((long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(msg);
    }

//    @GetMapping("/user/show/add_to_friend")

//    @GetMapping("/user/show/{user_id}")
//    @GetMapping("/user/friends")
//    @GetMapping("/user/ratings/")
//    @GetMapping("/user/reviews/")
//    @GetMapping("/user/bookshelves/{shelf_id}")
//    @GetMapping("/user/bookshelves/all")
}
