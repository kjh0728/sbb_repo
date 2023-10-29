package com.mysite.sbb.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
public class MypageAnswerPageDTO {
    private Page<MypageAnswerDTO> mypageAnswerDTOPage;
}
