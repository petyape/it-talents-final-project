package com.example.goodreads.controller;

import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import com.example.goodreads.model.dto.userDTO.UserWithAddressDTO;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
//    @DeleteMapping("user/destroy")

}
