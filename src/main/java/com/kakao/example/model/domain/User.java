package com.kakao.example.model.domain;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String password;
    private String name;
    private String email;
}
