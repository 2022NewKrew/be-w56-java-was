package service;

import dto.UserCreateDto;
import dto.UserItemDto;
import dto.UserSignInDto;
import model.User;

import java.util.List;

public interface MemberService {
    void create(UserCreateDto userCreateDto);

    User signIn(UserSignInDto userSignInDto);

    List<UserItemDto> getList();
}
