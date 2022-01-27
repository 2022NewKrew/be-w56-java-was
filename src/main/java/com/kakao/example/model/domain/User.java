package com.kakao.example.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;
}
