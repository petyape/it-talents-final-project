package com.example.goodreads.model.entities;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class UsersBooksKey implements Serializable {

    @Column
    long userId;

    @Column
    long bookId;

}
