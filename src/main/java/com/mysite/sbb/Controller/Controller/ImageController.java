package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Model.Entity.Image;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Service.ImageService;
import com.mysite.sbb.Service.MemberService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Builder
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;
    private final MemberService memberService;

    @PostMapping("/upload")
    public String upload(String url, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberService.getMember(userDetails.getUsername());
        imageService.upload(url, member);

        return "redirect:/member/mypage";
    }
}