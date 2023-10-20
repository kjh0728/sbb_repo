package com.mysite.sbb.Service;

import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);

        if (question.isPresent())
        {
            return question.get();
        }
        else
        {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, Member member)
    {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setMember(member);
        this.questionRepository.save(question);
    }

    public Page<Question> getPage(int page, String kw)
    {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));

        Specification<Question> specification = search(kw);
        return this.questionRepository.findAll(specification, pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }


    public void addLike(Question question, Member member) {
        question.getLikeMembers().add(member);
        this.questionRepository.save(question);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, Member> u1 = q.join("member", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("member", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
}
