package com.mysite.sbb.Model.Repository;

import com.mysite.sbb.Model.Entity.Image;
import com.mysite.sbb.Model.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByMember(Member member);
}
