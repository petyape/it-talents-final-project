package com.example.goodreads.model.entities;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adress_id")
    private long addressId;

    @Column
    private String townName;

    @Column
    private int regionCode;

    @Column
    private String zipCode;

    @Column
    private String countryName;

}
