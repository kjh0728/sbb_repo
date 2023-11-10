package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Query("SELECT qm.tag FROM QuestionTagMap qm GROUP BY qm.tag ORDER BY COUNT(qm.tag) DESC, qm.tag.id ASC LIMIT 5")
    List<Tag> getHotTag();

    @Query("SELECT COUNT(qm.tag) FROM QuestionTagMap qm GROUP BY qm.tag ORDER BY COUNT(qm.tag) DESC, qm.tag.id ASC LIMIT 5")
    List<Long> getHotTagCount();
}
