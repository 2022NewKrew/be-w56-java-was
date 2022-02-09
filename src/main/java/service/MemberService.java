package service;

import dto.UserCreateDto;
import dto.UserSignInDto;
import model.User;

public interface MemberService {
    void create(UserCreateDto userCreateDto);

    User signIn(UserSignInDto userSignInDto);
}
