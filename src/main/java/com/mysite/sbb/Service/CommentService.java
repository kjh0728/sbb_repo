package com.mysite.sbb.Service;

import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.DTO.AnswerCommentDTO;
import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.CommentRepository;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class CommentService {
    private CommentRepository commentRepository;

    public Comment create(Question question, String content, Member member)
    {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setQuestion(question);
        comment.setMember(member);
        comment.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(comment);
        return comment;
    }


    public Comment create(Question question, Answer answer, String content, Member member)
    {
        AnswerCommentDTO answerCommentDTO = new AnswerCommentDTO();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setQuestion(question);
        comment.setAnswer(answer);
        comment.setMember(member);
        comment.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(comment);
        return comment;
    }


    public Comment getComment(Long id)
    {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent())
        {
            return comment.get();
        }
        else
        {
            throw new DataNotFoundException("Comment not found");
        }
    }
    public Page<Comment> getPage(Question question, int page)
    {
        Pageable pageable = PageRequest.of(page, 3);

        return commentRepository.findByQuestionOrderByCreateDateDesc(pageable, question);
    }


    public Page<Comment> getPage(Answer answer, int page)
    {
        Pageable pageable = PageRequest.of(page, 3);

        return commentRepository.findByAnswerOrderByCreateDateDesc(pageable, answer);
    }

    public void delete(Comment comment)
    {
        this.commentRepository.delete(comment);
    }
}
