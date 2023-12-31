package com.mysite.sbb.Model.Form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionForm {

    private Long category;

    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max=200)
    private String subject;

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;

    private List<String> hashtag;
}
