package com.example.goodreads.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Setter
@NoArgsConstructor
@ResponseBody
public class LogoutUserDTO {

    private Integer userId;

}
