package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Category;
import com.mysite.sbb.Model.Entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> specification, Pageable pageable);

    @Modifying
    @Query("update Question q set q.view = q.view + 1 where q.id = :id")
    Long updateByView(@Param("id") Long id);
}
