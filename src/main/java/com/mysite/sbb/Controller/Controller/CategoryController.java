package com.mysite.sbb.Controller.Controller;

import com.mysite.sbb.Controller.Form.CategoryForm;
import com.mysite.sbb.Controller.Form.QuestionForm;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Service.CategoryService;
import com.mysite.sbb.Service.MemberService;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/category")
@Controller
@Builder
public class CategoryController {

    private CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String questionCreate(CategoryForm categoryForm)
    {
        return "category_form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String questionCreate(@Valid CategoryForm categoryForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "category_form";
        }
        this.categoryService.create(categoryForm.getName());
        return "redirect:/board/list";
    }

}
