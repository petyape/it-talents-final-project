package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regionId;
    @Column
    private String countryName;
    @OneToMany(mappedBy = "regions")
    private Set<Town> towns;
}
