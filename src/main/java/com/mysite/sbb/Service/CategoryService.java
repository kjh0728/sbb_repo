package com.mysite.sbb.Service;


import com.mysite.sbb.Model.Entity.Category;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.CategoryRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Builder
public class CategoryService {
    private CategoryRepository categoryRepository;

    public void create(String name)
    {
        Category category = new Category();
        category.setName(name);
        this.categoryRepository.save(category);
    }

    public List<Category> getList()
    {
        return this.categoryRepository.findAll();
    }

    public Category getCategory(Long id)
    {
        return this.categoryRepository.findById(id).orElse(null);
    }

}
