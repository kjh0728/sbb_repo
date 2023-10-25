package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Controller.Form.AnswerForm;
import com.mysite.sbb.Controller.Form.CommentForm;
import com.mysite.sbb.Controller.Form.QuestionForm;
import com.mysite.sbb.Model.DTO.AnswerCommentDTO;
import com.mysite.sbb.Model.DTO.AnswerCommentListDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/board")
@Controller
@Builder
public class QuestionController {

    private final QuestionService questionService;
    private final MemberService memberService;
    private final AnswerService answerService;
    private final CommentService commentService;
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw)
    {
        Page<Question> paging = this.questionService.getPage(page, kw);

        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}" , method = {RequestMethod.POST, RequestMethod.GET})
    public String detail(Model model, @PathVariable("id") Long id,
                         @RequestParam(value = "page_num", defaultValue = "0") int page_num,
                         @RequestParam(value = "qco_open", defaultValue = "false") boolean qco_open,
                         @RequestParam(value = "qco_page_num", defaultValue = "0") int qco_page_num,
                         @ModelAttribute(value="AnswerCommentListDTO") AnswerCommentListDTO answerCommentListDTO,
                         AnswerForm answerForm)
    {
        Question question = this.questionService.getQuestion(id);

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
    public String questionCreate(QuestionForm questionForm)
    {
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
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getMember().getUsername().equals(principal.getName())) {
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

    @RequestMapping(value = "/page" , method = {RequestMethod.POST, RequestMethod.GET})
    public String page(Model model, @ModelAttribute(value="AnswerCommentListDTO") AnswerCommentListDTO answerCommentListDTO) {
        System.out.println("리스트 테스트!!! 받았다!!!");
        System.out.println(answerCommentListDTO);
        return "redirect:/detail/";
    }
}
