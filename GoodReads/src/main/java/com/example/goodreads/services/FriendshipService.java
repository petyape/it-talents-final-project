package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;


@Service
public class FriendshipService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String addAsFriend(long userId, long friendId){
        if(userId == friendId){
            throw new BadRequestException("You can't become friends with yourself!");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        User friend = userRepository.findById(friendId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if(user.getFriends().contains(friend)){
            throw new BadRequestException("You are already friends!");
        }
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        return user.getFirstName() + " became friends with " + friend.getFirstName() + ".";
    }

    @Transactional
    public String removeFriend(long userId, long friendId){
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        User friend = userRepository.findById(friendId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if(userId == friendId){
            throw new BadRequestException("You can't unfriend yourself!");
        }
        if(!user.getFriends().contains(friend)){
            throw new BadRequestException("No such friend founded!");
        }
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        return "Removed from friends.";
    }

    public Set<User> getFriends(long id){
        User user = userRepository.findById(id).orElseThrow(() -> (new NotFoundException("User not found!")));
        return user.getFriends();
    }
}
