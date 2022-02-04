package mvc.service;

import mvc.db.DataBase;
import mvc.dto.UserDto;
import mvc.model.User;

public class UserService {

    public UserDto create(UserDto dto) {
        User newUser = User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
        DataBase.addUser(newUser);
        return dto;
    }

    public UserDto login(UserDto dto) {
        var dbUser = DataBase.findUserById(dto.getUserId());
        if (dbUser == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        validatePassword(dbUser, dto);
        return UserDto.builder()
                .userId(dbUser.getUserId())
                .password(dbUser.getPassword())
                .name(dbUser.getName())
                .email(dbUser.getEmail())
                .build();
    }

    private void validatePassword(User dbUser, UserDto requestUser) {
        if (!dbUser.getPassword().equals(requestUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다");
        }
    }
}
