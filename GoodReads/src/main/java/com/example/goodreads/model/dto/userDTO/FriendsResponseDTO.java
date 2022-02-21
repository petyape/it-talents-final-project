package com.example.goodreads.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FriendsResponseDTO {
    private Set<UserResponseDTO> friends;
}
