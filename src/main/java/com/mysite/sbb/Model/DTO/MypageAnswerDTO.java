package com.mysite.sbb.Model.DTO;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Question;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
public class MypageAnswerDTO {

    private Page<Answer> answer;

    private Page<Question> question;
}
