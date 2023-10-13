package com.mysite.sbb.Controller;

import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Service.QuestionService;
import lombok.Builder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Builder
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/board/list")
    public String list(Model model)
    {
        List<Question> questionList = this.questionService.getList();

        model.addAttribute("questionList", questionList);
        return "question_list";
    }
}
