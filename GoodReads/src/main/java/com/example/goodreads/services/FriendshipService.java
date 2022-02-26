package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FriendshipService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    @Transactional
    public UserResponseDTO addAsFriend(long userId, long friendId) {
        if(userId == friendId){
            throw new BadRequestException("Users cannot become friends with themselves!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        User friend = userRepository.findById(friendId).orElseThrow(() -> (new NotFoundException("User not found!")));

        if(user.getFriends().contains(friend)){
            throw new BadRequestException("This user is already in your friend list!");
        }
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        UserResponseDTO dto = mapper.map(friend, UserResponseDTO.class);
        return dto;
    }

    @Transactional
    public UserResponseDTO removeFriend(long userId, long friendId){
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        User friend = userRepository.findById(friendId).orElseThrow(() -> (new NotFoundException("User not found!")));

        if(userId == friendId){
            throw new BadRequestException("Users cannot unfriend themselves!");
        }
        if(!user.getFriends().contains(friend)){
            throw new BadRequestException("This user is not in your friend list!");
        }
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        UserResponseDTO dto = mapper.map(friend, UserResponseDTO.class);
        return dto;
    }

    public List<UserResponseDTO> getFriends(long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        List<UserResponseDTO> friendsListDTO = new ArrayList<>();
        Set<User> friends = user.getFriends();
        for (User friend : friends) {
            UserResponseDTO dto = mapper.map(friend, UserResponseDTO.class);
            friendsListDTO.add(dto);
        }
        return friendsListDTO;
    }

}
