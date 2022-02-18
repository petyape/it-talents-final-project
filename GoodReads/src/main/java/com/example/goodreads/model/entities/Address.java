package com.example.goodreads.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adress_id")
    private int addressId;

    @Column
    private String townName;

    @Column
    private int regionCode;

    @Column
    private String countryName;


}
