package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Config.UserRole;
import com.mysite.sbb.Controller.Form.AnswerForm;
import com.mysite.sbb.Controller.Form.CategoryForm;
import com.mysite.sbb.Controller.Form.CommentForm;
import com.mysite.sbb.Controller.Form.QuestionForm;
import com.mysite.sbb.Model.DTO.AnswerCommentDTO;
import com.mysite.sbb.Model.DTO.AnswerCommentListDTO;
import com.mysite.sbb.Model.DTO.ViewMemberDTO;
import com.mysite.sbb.Model.Entity.*;
import com.mysite.sbb.Service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Builder;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RequestMapping("/board")
@Controller
@Builder
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;
    private final AnswerService answerService;
    private final CommentService commentService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw)
    {
        Page<Question> paging = this.questionService.getPage(page, kw);
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }

    @GetMapping("/list/{id}")
    public String list(Model model, @PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw)
    {
        Category nowCategory = this.categoryService.getCategory(id);
        Page<Question> paging = this.questionService.getPage(nowCategory.getId(), page, kw);
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("nowCategory", nowCategory);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}" , method = {RequestMethod.POST, RequestMethod.GET})
    @Transactional
    public String detail(Model model, @PathVariable("id") Long id,
                         @RequestParam(value = "page_num", defaultValue = "0") int page_num,
                         @RequestParam(value = "qco_open", defaultValue = "false") boolean qco_open,
                         @RequestParam(value = "qco_page_num", defaultValue = "0") int qco_page_num,
                         @ModelAttribute(value="AnswerCommentListDTO") AnswerCommentListDTO answerCommentListDTO,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         Principal principal,
                         AnswerForm answerForm)
    {
        Question question = this.questionService.getQuestion(id);

        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }
        String username = null;

        if(principal != null)
        {
            username = principal.getName();
        }


        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + username+ "]["+ id.toString() +"]")) {
                this.questionService.updateView(id);

                oldCookie.setValue(oldCookie.getValue() + "_[" + username + "]["  + id + "]");

                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            this.questionService.updateView(id);
            Cookie newCookie;

            newCookie= new Cookie("postView", "[" + username + "]["  + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
            response.addCookie(newCookie);
        }


        model.addAttribute("question", question);
        model.addAttribute("qco_open", qco_open);

        Page<Comment> pageQuestComment = this.commentService.getPage(question, qco_page_num);
        model.addAttribute("pageQuestComment", pageQuestComment);


        Page<Answer> pageAnswer = this.answerService.getPage(question, page_num);
        model.addAttribute("pageAnswer", pageAnswer);

        if(answerCommentListDTO.getAnswerCommentDTOList() == null)
        {
            List<AnswerCommentDTO> answerCommentDTOList = answerService.getAnswerCommentDTO(pageAnswer);
            model.addAttribute("answerCommentDTOList", answerCommentDTOList);
        }
        else {
            List<AnswerCommentDTO> answerCommentDTOList = answerService.getAnswerCommentDTO(pageAnswer, answerCommentListDTO);
            model.addAttribute("answerCommentDTOList", answerCommentDTOList);
        }



        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm, Model model)
    {
        List<Category> categoryList = categoryService.getList();
        model.addAttribute("categoryList", categoryList);
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Member siteUser = this.memberService.getMember(principal.getName());
        Category category = this.categoryService.getCategory(questionForm.getCategory());
        this.questionService.create(category, questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Long id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated() or hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String questionDelete(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);

        if (!question.getMember().getUsername().equals(user.getUsername())
                && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        Member member = this.memberService.getMember(principal.getName());
        this.questionService.addLike(question, member);
        return String.format("redirect:/board/detail/%s", id);
    }
}
