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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.desktop.ScreenSleepEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

@Controller
@Builder
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;
    private final MemberService memberService;

    @PostMapping("/upload")
    public String upload(@RequestParam("img-input")MultipartFile multipartFile,
                         Authentication authentication) throws IOException, NoSuchAlgorithmException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberService.getMember(userDetails.getUsername());
        String currName = multipartFile.getOriginalFilename();
        assert currName != null;
        String[] type = multipartFile.getContentType().split("/");

        if(!type[type.length - 1].equals("octet-stream"))
        {
            String name = new MD5Generator(currName).toString() + "." +  type[type.length - 1];

            String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            String imgPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img";

            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }

            String filePath = savePath + "\\" + name;

            String strimgPath = imgPath + "\\myimg.png";

            File origFile = new File(filePath);
            multipartFile.transferTo(origFile);

            File myImg = new File(strimgPath);
            Files.copy(origFile.toPath(), myImg.toPath(), StandardCopyOption.REPLACE_EXISTING);

            imageService.upload(currName, name, filePath, member);

        }


        return "redirect:/member/mypage";
    }
}