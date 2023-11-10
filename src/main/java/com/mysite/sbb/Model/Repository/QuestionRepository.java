package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Category;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Entity.QuestionTagMap;
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
    Integer updateByView(@Param("id") Long id);


    @Modifying
    @Query("update Question q set q.view = q.view + 1 where q.id = :id")
    Long updateByScore(@Param("id") Long id);

    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join Member u1 on q.member=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join Member  u2 on a.member=u2 "
            + "left outer join QuestionTagMap qm on qm.question=q "
            + "right outer join Tag t on qm.tag = t "
            + "where "
            + "   t.name = :tag and q.subject like %:kw% "
            + "   or t.name = :tag and q.content like %:kw% "
            + "   or t.name = :tag and u1.username like %:kw% "
            + "   or t.name = :tag and a.content like %:kw% "
            + "   or t.name = :tag and u2.username like %:kw% ")

    Page<Question> findAllByKeywordAndTag(@Param("kw") String kw, @Param("tag")String tag, Pageable pageable);
}
