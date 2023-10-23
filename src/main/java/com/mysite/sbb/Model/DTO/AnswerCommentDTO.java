package com.mysite.sbb.Model.DTO;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class AnswerCommentDTO {

    private Answer parent;

    private Page<Comment> comment;

    private int pageNum;

    public AnswerCommentDTO()
    {
        this.pageNum = 0;
    }
}
