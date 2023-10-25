package com.mysite.sbb.Service;

import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.DTO.AnswerCommentDTO;
import com.mysite.sbb.Model.DTO.AnswerCommentListDTO;
import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.AnswerRepository;
import com.mysite.sbb.Model.Repository.CommentRepository;
import jakarta.persistence.OrderBy;
import lombok.Builder;
import org.springframework.data.domain.*;
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
}