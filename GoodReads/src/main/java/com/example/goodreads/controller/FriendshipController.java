package com.example.goodreads.controller;

import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import com.example.goodreads.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class FriendshipController extends BaseController {

    @Autowired
    private FriendshipService friendshipService;

    @PutMapping("/friends/add/{friendId}")
    public ResponseEntity<String> addAsFriend (@PathVariable long friendId,
                                               HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        String msg = friendshipService.addAsFriend((long) session.getAttribute(USER_ID), friendId);
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/friends/remove/{friendId}")
    public ResponseEntity<String> removeFriend (@PathVariable long friendId,
                                                HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        String msg = friendshipService.removeFriend((long) session.getAttribute(USER_ID), friendId);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/friends/show/{id}")
    public ResponseEntity<List<UserResponseDTO>> getFriends(@PathVariable long id,
                                                            HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        List<UserResponseDTO> userFriends = friendshipService.getFriends(id);
        return ResponseEntity.ok(userFriends);
    }

}
