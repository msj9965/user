package com.example.userservice.member.service;

import com.example.userservice.member.domain.Member;
import com.example.userservice.member.dto.MemberLoginReqDto;
import com.example.userservice.member.dto.MemberSignupDto;
import com.example.userservice.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member signup(MemberSignupDto memberSignupDto) {
        Member member = Member.builder()
                .email(memberSignupDto.getEmail())
                .password(passwordEncoder.encode(memberSignupDto.getPassword()))
        .build();
        memberRepository.save(member);
        return member;
    }

    @Override
    public Member login(MemberLoginReqDto memberLoginReqDto) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberLoginReqDto.getEmail());
        if(!optionalMember.isPresent()){
            throw new IllegalArgumentException("email이 존재하지 않습니다.");
        }
        Member member = optionalMember.get();
        if(!passwordEncoder.matches( memberLoginReqDto.getPassword(),member.getPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다.");
        }
        return member;
    }
}
