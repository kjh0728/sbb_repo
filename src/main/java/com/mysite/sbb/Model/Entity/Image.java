package com.mysite.sbb.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String currName;

    private String name;

    private String url;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member member;
}
