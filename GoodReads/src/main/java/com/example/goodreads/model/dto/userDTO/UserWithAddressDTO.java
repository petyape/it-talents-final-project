package com.example.goodreads.model.dto.userDTO;

import com.example.goodreads.model.entities.Address;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.services.util.Helper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserWithAddressDTO {

    private long userId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private char gender;
    private String username;
    private Boolean showLastName;
    private Boolean isReverseNameOrder;
    private char genderViewableBy;
    private char locationViewableBy;
    private LocalDate dateOfBirth;
    private String webSite;
    private String interests;
    private String booksPreferences;
    private String aboutMe;
    private Address address;

    public boolean isValid() {
        return (Helper.isValidEmail(email) && !firstName.isBlank() && User.Gender.isValidGender(gender) &&
                Helper.Visibility.isValidVisibility(genderViewableBy) &&
                Helper.Visibility.isValidVisibility(locationViewableBy) && address != null);
    }

}
