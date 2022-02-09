package service;

import dto.user.UserCreateDto;
import dto.user.UserItemDto;
import dto.user.UserSessionedDto;
import dto.user.UserSignInDto;
import model.User;

import java.util.List;

public interface MemberService {
    void create(UserCreateDto userCreateDto);

    UserSessionedDto signIn(UserSignInDto userSignInDto);

    List<UserItemDto> getList();
}
