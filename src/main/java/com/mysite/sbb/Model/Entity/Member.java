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

    @Column(length = 50)
    private String realName;

    @Column(length = 50)
    private String nickName;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image image;

    private String provider;
    private String providerId;

    private String snsImage;

    @Column(nullable = false)
    private Long score;
}
