package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Model.Form.CommentForm;
import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.CommentService;
import com.mysite.sbb.Service.MemberService;
import com.mysite.sbb.Service.QuestionService;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/comment")
@Controller
@Builder
public class CommentController {

    private CommentService commentService;
    private QuestionService questionService;
    private AnswerService answerService;
    private MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/create/{id}")
    public String createCommentQuestion(Model model, @PathVariable("id") Long id,
                                @Valid CommentForm commentForm, BindingResult bindingResult,
                                Principal principal)
    {
        Question question = this.questionService.getQuestion(id);

        if(bindingResult.hasErrors())
        {
            model.addAttribute("question", question);
            return "question_detail";
        }

        Member member = this.memberService.getMember(principal.getName());

        Comment comment = this.commentService.create(question, commentForm.getContent(), member);

        return String.format("redirect:/board/detail/%s?qco=true#qco_%s", comment.getQuestion().getId(), comment.getId());
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/create/{qid}/{aid}")
    public String createCommentAnswer(Model model, @PathVariable("qid") Long qid, @PathVariable("aid") Long aid,
                                      @Valid CommentForm commentForm, BindingResult bindingResult,
                                      Principal principal)
    {
        Answer answer = this.answerService.getAnswer(aid);
        Question question = this.questionService.getQuestion(qid);
        if(bindingResult.hasErrors())
        {
            model.addAttribute("answer", answer);
            return "question_detail";
        }

        Member member = this.memberService.getMember(principal.getName());

        Comment comment = this.commentService.create(question, answer, commentForm.getContent(), member);

        return String.format("redirect:/board/detail/%s?aco=true&&answer_id=%d#aco_%s", comment.getQuestion().getId(), aid, comment.getId());
    }


    @PreAuthorize("isAuthenticated() or hasRole('ROLE_ADMIN')")
    @GetMapping("/question/delete/{id}")
    public String QuestionDelete(@AuthenticationPrincipal User user,
                                 Principal principal,
                                 @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getMember().getUsername().equals(principal.getName())
                && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/board/detail/%s?qco=true", comment.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated() or hasRole('ROLE_ADMIN')")
    @GetMapping("/answer/delete/{id}")
    public String answerDelete(@AuthenticationPrincipal User user,
                               Principal principal,
                               @PathVariable("id") Long id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getMember().getUsername().equals(principal.getName())
                && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/board/detail/%s?aco=true", comment.getQuestion().getId());
    }
}
