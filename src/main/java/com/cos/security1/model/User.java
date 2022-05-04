package com.cos.security1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;


@Data //getter setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String password;
    private String email;
    private String role;//ROLE_USER, ROLE_ADMIN

    @CreationTimestamp
    private Timestamp createDate;
}
