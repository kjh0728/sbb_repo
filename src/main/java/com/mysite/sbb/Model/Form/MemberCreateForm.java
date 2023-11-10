package com.mysite.sbb.Model.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {
    @Size(min=3, max=100)
    @NotEmpty(message = "사용자 ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

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
