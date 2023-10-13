package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.QuestionRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList()
    {
        return this.questionRepository.findAll();
    }
}
