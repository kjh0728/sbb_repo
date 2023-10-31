package com.mysite.sbb.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@Entity
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "TEXT")
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Image image;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    @OrderBy("createDate desc")
    private List<Answer> answerList;

    private String provider;
    private String providerId;
}
