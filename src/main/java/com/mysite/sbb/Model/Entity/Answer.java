package com.mysite.sbb.Model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.web.PageableDefault;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Member> likeMembers;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    @OrderBy("createDate desc")
    private List<Comment> commentList;
}
