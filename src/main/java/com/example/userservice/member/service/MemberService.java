package com.example.userservice.member.service;

import com.example.userservice.member.domain.Member;
import com.example.userservice.member.dto.MemberLoginReqDto;
import com.example.userservice.member.dto.MemberSignupDto;

public interface MemberService {
    Member signup(MemberSignupDto memberSignupDto);
    Member login(MemberLoginReqDto memberLoginReqDto);

}
