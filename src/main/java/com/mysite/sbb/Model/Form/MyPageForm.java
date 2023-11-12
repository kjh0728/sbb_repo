package com.mysite.sbb.Model.Form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageForm {

    @NotEmpty(message = "실명은 필수항목입니다.")
    private String realName;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickName;

    @NotEmpty(message = "email은 필수항목입니다.")
    private String email;
}
