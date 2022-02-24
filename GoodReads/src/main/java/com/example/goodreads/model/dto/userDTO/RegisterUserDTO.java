package com.example.goodreads.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Setter
@NoArgsConstructor
@ResponseBody
public class RegisterUserDTO {

    private String email;
    private String firstName;
    private String password;
    private String confirmPassword;

}
