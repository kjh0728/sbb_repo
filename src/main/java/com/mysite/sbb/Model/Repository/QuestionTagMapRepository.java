package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.QuestionTagMap;
import com.mysite.sbb.Model.Entity.Tag;
import jakarta.persistence.OrderBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagMapRepository extends JpaRepository<QuestionTagMap,Long> {
    int countByTag(Tag tag);

    List<QuestionTagMap> findByTag(Tag tag);
}
