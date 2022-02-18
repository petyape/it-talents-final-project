package com.example.goodreads.model.dto;

import lombok.Data;

@Data
public class AddressWithoutUserDTO {
    private int addressId;
    private String townName;
    private int regionCode;
    private String countryName;
}
