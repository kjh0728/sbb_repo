package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Controller.Form.MemberCreateForm;
import com.mysite.sbb.Controller.Form.PWModifyForm;
import com.mysite.sbb.Controller.Form.TempPasswordForm;
import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.Entity.*;
import com.mysite.sbb.Service.*;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/member")
@Builder
public class MemberController {
    private final MemberService memberService;
    private final ImageService imageService;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final AnswerService answerService;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "/member/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/member/signup_form";
        }

        if (!memberCreateForm.getPassword().equals(memberCreateForm.getRe_password())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "/member/signup_form";
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
            return "/member/signup_form";
        }
        catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/member/signup_form";
        }


        return "redirect:/";
    }

    @GetMapping("/login")
    public String login()
    {
        return "/member/login_form";
    }

    @GetMapping("/findpw")
    public String findpw(TempPasswordForm tempPasswordForm)
    {
        return "/member/findpw_form";
    }


    @PostMapping("/findpw")
    public String sendTempPassword(@Valid TempPasswordForm tempPasswordForm,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/member/findpw_form";
        }

        try {
            memberService.modifyPassword(tempPasswordForm.getEmail());
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            bindingResult.reject("emailNotFound", e.getMessage());
            return "/member/findpw_form";
        } catch (EmailException e) {
            e.printStackTrace();
            bindingResult.reject("sendEmailFail", e.getMessage());
            return "/member/findpw_form";
        }
        return "redirect:/member/login";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, Principal principal) throws InterruptedException {
        Member member = memberService.getMember(principal.getName());
        Image image = imageService.findImage(member);
        model.addAttribute("img", image);
        return "/member/mypage_form";
    }

    @GetMapping("/pw_modify")
    public String pw_modify(PWModifyForm pwModifyForm , Model model)
    {
        model.addAttribute("pwModifyForm", pwModifyForm);
        return "/member/pw_modify_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/pw_modify")
    public String pw_modify(@Valid PWModifyForm pwModifyForm, BindingResult bindingResult, Principal principal)
    {
        if(bindingResult.hasErrors())
        {
            return "/member/pw_modify_form";
        }


        if (!pwModifyForm.getPassword().equals(pwModifyForm.getRe_password())) {
            bindingResult.rejectValue("password-noMatch", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "/member/pw_modify_form";
        }


        try
        {
            memberService.modify(principal.getName(), pwModifyForm.getPassword());
        }
        catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("pw_modifyFailed", e.getMessage());
            return "/member/pw_modify_form";
        }

        return "/member/mypage_form";
    }

    @GetMapping("/mypage_question")
    public String mypage_question(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "kw", defaultValue = "") String kw,
                                  Principal principal)
    {
        Page<Question> paging = this.questionService.getPage(principal.getName(), page, kw);
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }

    @GetMapping("/mypage_answer")
    public String mypage_answer(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "kw", defaultValue = "") String kw,
                                  Principal principal)
    {
        Page<Answer> paging = this.answerService.getPage(principal.getName(), page, kw);
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "answer_list";
    }

}
