package com.mysite.sbb.Model.Form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {
    @NotEmpty(message = "카테고리 이름은 필수 항목입니다.")
    @Size(max=50)
    private String name;
}
