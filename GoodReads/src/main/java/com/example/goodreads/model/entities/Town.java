package com.example.goodreads.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "towns")
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int townId;
    @Column
    private String townName;
    @Column
    private String zipCode;
    @ManyToOne
    @JoinColumn(name = "region_code", referencedColumnName = "region_id")
    private Region region;
}
