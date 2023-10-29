package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Config.MD5Generator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller
@Builder
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;
    private final MemberService memberService;

    @PostMapping("/upload")
    public String upload(@RequestParam("img-input")MultipartFile file, Authentication authentication) throws IOException, NoSuchAlgorithmException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberService.getMember(userDetails.getUsername());
        String currName = file.getOriginalFilename();
        assert currName != null;
        String[] type = file.getContentType().split("/");

        String name = new MD5Generator(currName).toString() + "." +  type[type.length - 1];

        String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        if (!new File(savePath).exists()) {
            try{
                new File(savePath).mkdir();
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }

        String filePath = savePath + "\\" + name;
        file.transferTo(new File(filePath));

        imageService.upload(currName, name, filePath, member);

        return "redirect:/member/mypage";
    }
}