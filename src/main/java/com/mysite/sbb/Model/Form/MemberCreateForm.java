package com.mysite.sbb.Model.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {
    @Size(min=4, message = "아이디는 4자 이상 입력해 주세요.")
    @NotEmpty(message = "아이디는 필수항목입니다.")
    private String username;

    @Size(min=8, message = "비밀번호는 8자 이상 입력해 주세요.")
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @Size(min=8, message = "비밀번호는 8자 이상 입력해 주세요.")
    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String re_password;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @NotEmpty(message = "실명은 필수항목입니다.")
    private String realName;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickName;

    private String provider;

    private String providerID;

    private String snsImage;
}
