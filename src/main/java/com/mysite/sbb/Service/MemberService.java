package com.mysite.sbb.Service;

import com.mysite.sbb.Config.CommonUtil;
import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.Entity.Answer;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final CommonUtil commonUtil;
    private final FindPasswordService findPasswordService;

    public Member create(String username, String email, String password)
    {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(member);
        return member;
    }

    public void modify(String username, String password)
    {
        Member member = memberRepository.findByUsername(username).orElse(null);
        assert member != null;
        member.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(member);
    }

    public Member getMember(String username)
    {
        Optional<Member> member = this.memberRepository.findByUsername(username);

        if(member.isPresent())
        {
            return  member.get();
        }
        else
        {
            throw new DataNotFoundException("member not found");
        }
    }

    @Transactional
    public void modifyPassword(String email) throws EmailException {
        String tempPassword = commonUtil.createTempPassword();
        Member user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("해당 이메일의 유저가 없습니다."));
        user.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(user);
        findPasswordService.sendSimpleMessage(email, tempPassword);
    }
}
