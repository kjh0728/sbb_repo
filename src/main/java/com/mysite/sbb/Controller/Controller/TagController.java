package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Model.Entity.*;
import com.mysite.sbb.Service.*;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Builder
@RequestMapping("/taged")
public class TagController {
    private final QuestionTagMapService tagMapService;
    private final CategoryService categoryService;
    private final QuestionService questionService;
    private final TagService tagService;
    private final MemberService memberService;

    @GetMapping("/list/{tagName}")
    public String taglist(Model model, @PathVariable("tagName")String tagName,
                          @RequestParam(value = "category", defaultValue = "0") Long category,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "kw", defaultValue = "") String kw)
    {
        List<Tag> hotTag = tagService.getHotTag();
        List<Long> hotTagCount = tagService.getHotTagCount();
        List<Member> topUser = memberService.getTopUser();

        Page<Question> paging = this.questionService.getPage(page, kw, tagName);
        List<Category> categoryList = this.categoryService.getList();

        model.addAttribute("hotTag", hotTag);
        model.addAttribute("topUser", topUser);
        model.addAttribute("hotTagCount", hotTagCount);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("category", category);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "question_list";
    }

}
