package com.mysite.sbb.Model.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ViewMemberDTO {
    private String username;

    private Long id;

    public ViewMemberDTO(String username, Long id)
    {
        this.username = username;
        this.id = id;
    }

}
