package com.mysite.sbb.Service;

import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.Entity.*;
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

    public void create(Category category, String subject, String content, Member member)
    {
        Question question = new Question();
        question.setCategory(category);
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setMember(member);
        this.questionRepository.save(question);
    }

    public Page<Question> getPage(Long category, int page, String kw)
    {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));

        Specification<Question> specification = search(kw, category);
        return this.questionRepository.findAll(specification, pageable);
    }

    public Page<Question> getPage(int page, String kw)
    {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));

        Specification<Question> specification = search(kw, 0L);
        return this.questionRepository.findAll(specification, pageable);
    }

    public Page<Question> getPage(String username, int page, String kw)
    {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));

        Specification<Question> specification = search(username, kw, 0L);
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

    public Long updateView(Long id)
    {
        return this.questionRepository.updateByView(id);
    }


    private Specification<Question> search(String kw, Long category) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, Member> u1 = q.join("member", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Question, Category> c = q.join("category", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("member", JoinType.LEFT);

                if(category == 0L)
                {
                    return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                            cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                            cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                            cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                            cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
                }
                else {
                    return cb.or(cb.and(cb.like(q.get("subject"), "%" + kw + "%"), cb.equal(c.get("id"), category)), // 제목
                            cb.and(cb.like(q.get("content"), "%" + kw + "%"), cb.equal(c.get("id"), category)),      // 내용
                            cb.and(cb.like(u1.get("username"), "%" + kw + "%"), cb.equal(c.get("id"), category)),    // 질문 작성자
                            cb.and(cb.like(a.get("content"), "%" + kw + "%"), cb.equal(c.get("id"), category)),      // 답변 내용
                            cb.and(cb.like(u2.get("username"), "%" + kw + "%"), cb.equal(c.get("id"), category)));   // 답변 작성자
                }
            }
        };
    }


    private Specification<Question> search(String username, String kw, Long category) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, Member> u1 = q.join("member", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Question, Category> c = q.join("category", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("member", JoinType.LEFT);


                return cb.and(cb.equal(u1.get("username"), username),
                        cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                                cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                                cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                                cb.like(a.get("content"), "%" + kw + "%")));     // 답변 내용

//                 else {
//                    return cb.or(cb.and(cb.like(q.get("subject"), "%" + kw + "%"), cb.equal(c.get("id"), category)), // 제목
//                            cb.and(cb.like(q.get("content"), "%" + kw + "%"), cb.equal(c.get("id"), category)),      // 내용
//                            cb.and(cb.like(u1.get("username"), "%" + kw + "%"), cb.equal(c.get("id"), category)),    // 질문 작성자
//                            cb.and(cb.like(a.get("content"), "%" + kw + "%"), cb.equal(c.get("id"), category)),      // 답변 내용
//                            cb.and(cb.like(u2.get("username"), "%" + kw + "%"), cb.equal(c.get("id"), category)));   // 답변 작성자
//                }

            }
        };
    }
}
