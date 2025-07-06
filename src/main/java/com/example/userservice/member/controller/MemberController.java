package com.example.userservice.member.controller;

import com.example.userservice.common.auth.JwtTokenProvider;
import com.example.userservice.member.domain.Member;
import com.example.userservice.member.dto.MemberLoginReqDto;
import com.example.userservice.member.dto.MemberLoginResDto;
import com.example.userservice.member.dto.MemberSignupDto;
import com.example.userservice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?>memberCreate(@RequestBody MemberSignupDto memberSignupDto){
        Member member = memberService.signup(memberSignupDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?>memberLogin(@RequestBody MemberLoginReqDto memberLoginReqDto){

        //email, password 일치하는지 검증
        Member member = memberService.login(memberLoginReqDto);

        //일치할 경우 jwt accessToken, refreshToken 생성
        String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail(), member.getRole().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());
        //accessToken, refreshToken을 응답
        MemberLoginResDto memberLoginResDto = new MemberLoginResDto(member.getId(), accessToken, refreshToken);
        return new ResponseEntity<>(memberLoginResDto ,HttpStatus.OK);

    }

}
