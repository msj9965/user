package com.example.userservice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginResDto {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
