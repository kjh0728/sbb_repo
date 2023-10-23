package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Comment;
import com.mysite.sbb.Model.Entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByAnswerOrderByCreateDateDesc(Pageable pageable, Answer answer);

    Page<Comment> findByQuestionOrderByCreateDateDesc(Pageable pageable, Question question);

    Comment findByAnswerId(Long id);
}
