package com.example.goodreads.controller;

import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.userDTO.LoginUserDTO;
import com.example.goodreads.model.dto.userDTO.RegisterUserDTO;
import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";

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
        validateLogin(session, request);
        User u = userService.editProfile(userEdited, session);
        UserResponseDTO dto = mapper.map(u, UserResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    // TODO: extract in a base class
    public static void validateLogin(HttpSession session, HttpServletRequest request) {
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean)session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if (newSession || !logged || !sameIP) {
            throw new UnauthorizedException("Please, login!");
        }
    }

//    @PutMapping("/user/change_password")
//    @PutMapping("user/edit/privacy")
//    @PostMapping("/user/upload_photo")
//    @DeleteMapping("user/destroy")

}
