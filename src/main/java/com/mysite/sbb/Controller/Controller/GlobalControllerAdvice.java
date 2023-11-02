package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Service.MemberService;
import lombok.Builder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@Builder
public class GlobalControllerAdvice {

    private MemberService memberService;

    @ModelAttribute
    public void addAttributes(Principal principal, Model model) {
        // 여기에 모든 컨트롤러에 추가하고 싶은 attribute를 추가합니다

        if(principal != null)
        {
            Member member = memberService.getMember(principal.getName());
            model.addAttribute("myphoto", member.getImage());
        }

    }
}