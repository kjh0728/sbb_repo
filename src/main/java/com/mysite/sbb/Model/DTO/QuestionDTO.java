package com.mysite.sbb.Model.DTO;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Question;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class QuestionDTO {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private List<Answer> answerList;

    public Question toEntity()
    {
        return Question.builder()
                .id(id)
                .subject(subject)
                .content(content)
                .createDate(createDate)
                .answerList(answerList)
                .build();
    }
}
