package com.mysite.sbb.Service;

import com.mysite.sbb.Config.CommonUtil;
import com.mysite.sbb.Model.Form.MemberCreateForm;
import com.mysite.sbb.Exception.DataNotFoundException;
import com.mysite.sbb.Model.Entity.Member;
import com.mysite.sbb.Model.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Builder
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final CommonUtil commonUtil;
    private final FindPasswordService findPasswordService;

    public Member create(MemberCreateForm memberCreateForm, boolean snsImage)
    {
        Member member = new Member();
        member.setUsername(memberCreateForm.getUsername());
        member.setEmail(memberCreateForm.getEmail());
        member.setPassword(passwordEncoder.encode(memberCreateForm.getPassword()));
        member.setRealName(memberCreateForm.getRealName());
        member.setNickName(memberCreateForm.getNickName());
        member.setProvider(memberCreateForm.getProvider());
        member.setProviderId(memberCreateForm.getProviderID());
        member.setScore(0L);

        if(snsImage)
        {
            member.setSnsImage(memberCreateForm.getSnsImage());
        }

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

    public void snsImageDelet(Member member)
    {
        member.setSnsImage(null);
        this.memberRepository.save(member);
    }

    public void updateScore(Member member)
    {
        member.setScore(member.getScore()+1);
        this.memberRepository.save(member);
    }

    public void downScore(Member member)
    {
        member.setScore(member.getScore()-1);
        this.memberRepository.save(member);
    }

    public void updateUser(String userName, String realName, String nickName)
    {
        Member member = getMember(userName);
        member.setRealName(realName);
        member.setNickName(nickName);
        memberRepository.save(member);
    }
    public List<Member> getTopUser()
    {
        return memberRepository.getTopUser();
    }

    public void delete(String username)
    {
        Member member = getMember(username);

        memberRepository.delete(member);
    }
}
