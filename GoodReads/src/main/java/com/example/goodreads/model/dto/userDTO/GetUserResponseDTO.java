package com.example.goodreads.model.dto.userDTO;

import com.example.goodreads.model.entities.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GetUserResponseDTO {

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private char gender;

    private String username;

    private LocalDate dateOfBirth;

    private String webSite;

    private String interests;

    private String booksPreferences;

    private String aboutMe;

    private Address address;

    private long reviews;
    // double average rating
    // REVIEWS - LIST WITH DTO

    private long ratings;
    // RATINGS - LIST WITH DTO

//    private List<User> friends;
//     DTO

}
