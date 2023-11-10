package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);

    @Query("SELECT m FROM Member m ORDER BY m.score DESC, m.nickName ASC LIMIT 5")
    List<Member> getTopUser();
}
