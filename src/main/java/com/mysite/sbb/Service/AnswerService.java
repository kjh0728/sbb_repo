package com.mysite.sbb.Service;

import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.DTO.AnswerCommentDTO;
import com.mysite.sbb.Model.DTO.AnswerCommentListDTO;
import com.mysite.sbb.Model.Entity.*;
import com.mysite.sbb.Model.Repository.AnswerRepository;
import com.mysite.sbb.Model.Repository.CommentRepository;
import jakarta.persistence.OrderBy;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class AnswerService {
    private final AnswerRepository answerRepository;

    private final CommentService commentService;
    public Answer create(Question question, String content, Member member)
    {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setMember(member);
        this.answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public List<AnswerCommentDTO> getAnswerCommentDTO(Page<Answer> answerPage)
    {
        List<AnswerCommentDTO> answerCommentDTOList = new ArrayList<>();
        for(Answer answer : answerPage.getContent())
        {
            AnswerCommentDTO answerCommentDTO = new AnswerCommentDTO();
            answerCommentDTO.setComment(commentService.getPage(answer, answerCommentDTO.getPageNum()));
            answerCommentDTO.setParent(answer);

            answerCommentDTOList.add(answerCommentDTO);
        }
        return answerCommentDTOList;
    }

    public List<AnswerCommentDTO> getAnswerCommentDTO(Page<Answer> answerPage, AnswerCommentListDTO answerCommentListDTO)
    {
        for(int i = 0; i < answerPage.getSize(); i++)
        {
            if(answerCommentListDTO.getAnswerCommentDTOList().size()-1 >= i)
            {
                answerCommentListDTO.getAnswerCommentDTOList().get(i).setComment(commentService.getPage(answerPage.getContent().get(i),  answerCommentListDTO.getAnswerCommentDTOList().get(i).getPageNum()));
                answerCommentListDTO.getAnswerCommentDTOList().get(i).setParent(answerPage.getContent().get(i));
            }
            else {
                AnswerCommentDTO answerCommentDTO = new AnswerCommentDTO();
                answerCommentDTO.setComment(commentService.getPage(answerPage.getContent().get(i), answerCommentDTO.getPageNum()));
                answerCommentDTO.setParent(answerPage.getContent().get(i));

                answerCommentListDTO.getAnswerCommentDTOList().add(answerCommentDTO);
            }
        }
        return answerCommentListDTO.getAnswerCommentDTOList();
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, Member member) {
        answer.getLikeMembers().add(member);
        this.answerRepository.save(answer);
    }

    public Page<Answer> getPage(Question question, int page)
    {
        Pageable pageable = PageRequest.of(page, 3);

        return this.answerRepository.findByQuestionOrderByLikeMembersDescCreateDateDesc(pageable, question);
    }

    public Page<Answer> getPage(String username, int page, String kw)
    {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));

        Specification<Answer> specification = search(username, kw, 0L);
        return this.answerRepository.findAll(specification, pageable);
    }

    private Specification<Answer> search(String username, String kw, Long category) {
        return new Specification<Answer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Answer> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Answer, Member> u1 = q.join("member", JoinType.LEFT);
                Join<Answer, Question> a = q.join("question", JoinType.LEFT);
                Join<Answer, Category> c = a.join("category", JoinType.LEFT);
                Join<Question, Member> u2 = a.join("member", JoinType.LEFT);


                return cb.and(cb.equal(u1.get("username"), username),
                        cb.or(cb.like(a.get("subject"), "%" + kw + "%"), // 제목
                                cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                                cb.like(u2.get("username"), "%" + kw + "%"),    // 질문 작성자
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
