package com.example.goodreads.controller;

import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import com.example.goodreads.services.FriendshipService;
import com.example.goodreads.services.util.Helper;
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
    public ResponseEntity<UserResponseDTO> addAsFriend (@PathVariable long friendId,
                                               HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        UserResponseDTO dto = friendshipService.addAsFriend((long) session.getAttribute(USER_ID), friendId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/friends/remove/{friendId}")
    public ResponseEntity<UserResponseDTO> removeFriend (@PathVariable long friendId,
                                                HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        UserResponseDTO dto = friendshipService.removeFriend((long) session.getAttribute(USER_ID), friendId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/friends/show/{id}")
    public ResponseEntity<PageDTO> getFriends( @PathVariable long id, @RequestParam int page,
                                              HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        Helper.validatePage(page);
        List<UserResponseDTO> userFriends = friendshipService.getFriends(id);
        PageDTO pageDTO = Helper.createPage(userFriends, page);
        return ResponseEntity.ok(pageDTO);
    }

}
