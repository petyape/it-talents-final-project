package com.example.goodreads.model.dto.userDTO;

import com.example.goodreads.model.dto.quoteDTO.QuoteResponseDTO;
import com.example.goodreads.model.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ResponseBody
public class GetUserDTO {

    private long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Address address;
    private char gender;
    private List<UserResponseDTO> friends;
    private long numberOfRatings;
    private double averageRatings;
    private long numberOfReviews;
    private long read;
    private long currentlyReading;
    private long wantToRead;
    private List<QuoteResponseDTO> quotes;

}
