package com.example.goodreads.controller;

import com.example.goodreads.model.dto.userDTO.FriendsResponseDTO;
import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.services.FriendshipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@RestController
public class FriendshipController extends BaseController {

    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/friend/add_as_friend/{friendId}")
    public ResponseEntity<String> addAsFriend (@PathVariable long friendId, HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        long userId = (long)session.getAttribute(USER_ID);
        String msg = friendshipService.addAsFriend(friendId, (userId));
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/friend/remove_friend/{friendId}")
    public ResponseEntity<String> removeFriend (@PathVariable long friendId, HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        long userId = (long)session.getAttribute(USER_ID);
        String msg = friendshipService.removeFriend(friendId, (userId));
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/friend/show/{id}")
    public ResponseEntity<FriendsResponseDTO> getFriends(@PathVariable long id, HttpSession session, HttpServletRequest request){
        UserController.validateSession(session, request);
        Set<User> userFriends = friendshipService.getFriends(id);
        Set<UserResponseDTO> friendsDTO= new HashSet<>();
        for (User user : userFriends) {
            UserResponseDTO dto = mapper.map(user, UserResponseDTO.class);
            friendsDTO.add(dto);
        }
        FriendsResponseDTO friendsResponseDTO = new FriendsResponseDTO();
        friendsResponseDTO.setFriends(friendsDTO);
        return ResponseEntity.ok(friendsResponseDTO);
    }
}
