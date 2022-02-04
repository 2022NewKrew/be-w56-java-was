package com.kakao.webserver.dto;

import com.kakao.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDto {
    private final int rowNum;
    private final User user;
}
