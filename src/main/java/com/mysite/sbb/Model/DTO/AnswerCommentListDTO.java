package com.mysite.sbb.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AnswerCommentListDTO {

    private List<AnswerCommentDTO> answerCommentDTOList;
}
