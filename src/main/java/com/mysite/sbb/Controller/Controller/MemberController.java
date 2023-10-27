package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Controller.Form.MemberCreateForm;
import com.mysite.sbb.Controller.Form.PWModifyForm;
import com.mysite.sbb.Controller.Form.TempPasswordForm;
import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Service.EmailException;
import com.mysite.sbb.Service.FindPasswordService;
import com.mysite.sbb.Service.MemberService;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/member")
@Builder
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!memberCreateForm.getPassword().equals(memberCreateForm.getRe_password())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try
        {
            String a =memberCreateForm.getPassword();
            memberService.create(memberCreateForm.getUsername(),
                    memberCreateForm.getEmail(), a);
        }
        catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "signup_form";
        }
        catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }


        return "redirect:/";
    }

    @GetMapping("/login")
    public String login()
    {
        return "login_form";
    }

    @GetMapping("/findpw")
    public String findpw(TempPasswordForm tempPasswordForm)
    {
        return "findpw_form";
    }


    @PostMapping("/findpw")
    public String sendTempPassword(@Valid TempPasswordForm tempPasswordForm,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "findpw_form";
        }

        try {
            memberService.modifyPassword(tempPasswordForm.getEmail());
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            bindingResult.reject("emailNotFound", e.getMessage());
            return "findpw_form";
        } catch (EmailException e) {
            e.printStackTrace();
            bindingResult.reject("sendEmailFail", e.getMessage());
            return "findpw_form";
        }
        return "redirect:/member/login";
    }

    @GetMapping("/mypage")
    public String mypage()
    {
        return "mypage_form";
    }

    @GetMapping("/pw_modify")
    public String pw_modify(PWModifyForm pwModifyForm , Model model)
    {
        model.addAttribute("pwModifyForm", pwModifyForm);
        return "pw_modify_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/pw_modify")
    public String pw_modify(@Valid PWModifyForm pwModifyForm, BindingResult bindingResult, Principal principal)
    {
        if(bindingResult.hasErrors())
        {
            return "pw_modify_form";
        }


        if (!pwModifyForm.getPassword().equals(pwModifyForm.getRe_password())) {
            bindingResult.rejectValue("password-noMatch", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "pw_modify_form";
        }


        try
        {
            memberService.modify(principal.getName(), pwModifyForm.getPassword());
        }
        catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("pw_modifyFailed", e.getMessage());
            return "pw_modify_form";
        }

        return "mypage_form";
    }
}
