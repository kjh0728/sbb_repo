package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Entity.*;
import com.mysite.sbb.Model.Repository.QuestionTagMapRepository;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Builder
public class QuestionTagMapService {

    private final QuestionTagMapRepository questionTagMapRepository;

    private final TagService tagService;

    public void create(Question question, List<String> tagList)
    {
       for(String t : tagList)
       {
           QuestionTagMap questionTagMap = new QuestionTagMap();

           questionTagMap.setQuestion(question);
           String[] tagname = t.split("\"");
           Tag tag = tagService.create(tagname[3]);

           questionTagMap.setTag(tag);
           questionTagMapRepository.save(questionTagMap);
       }
    }

    public List<Question> getList(String tagName, int page, String kw, Long category)
    {
        Tag tag = tagService.getTag(tagName);
        List<QuestionTagMap> tagMaps = questionTagMapRepository.findByTag(tag);

        List<Question> questions = new ArrayList<>();
        for(QuestionTagMap tm : tagMaps)
        {
            questions.add(tm.getQuestion());
        }

        return questions;
    }
}
