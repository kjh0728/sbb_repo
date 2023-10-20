package com.mysite.sbb.Service;

import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Builder
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String email, String password)
    {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(member);
        return member;
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
}
