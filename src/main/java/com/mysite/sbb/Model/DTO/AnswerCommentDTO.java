package com.mysite.sbb.Model.DTO;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
public class AnswerCommentDTO {

    private Answer parent;

    private Page<Comment> comment;

    private int pageNum;

    private boolean open;

    public AnswerCommentDTO()
    {
        this.pageNum = 0;
        this.open = false;
    }
}
