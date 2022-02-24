package com.example.goodreads.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reading_challenge")
public class ReadingChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long entryId;

    @Column
    private int booksGoal;

    @OneToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "user_id")
    private User user;

}
