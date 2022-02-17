package com.example.goodreads.model.entities;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class UsersBooksKey implements Serializable {
    @Column
    int userId;
    @Column
    int bookId;
}
