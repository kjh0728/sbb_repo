package com.mysite.sbb.Model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String subject;

    @Column(columnDefinition="TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @OrderBy("createDate desc")
    private List<Answer> answerList;

    @ManyToMany
    Set<Member> likeMembers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @OrderBy("createDate desc")
    @Where(clause = "answer_id is null")
    private List<Comment> commentList;
}
