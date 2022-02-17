package com.example.goodreads.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "address")
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

    @OneToOne
    @JoinColumn(name = "adress_id")
    private User user;
}
