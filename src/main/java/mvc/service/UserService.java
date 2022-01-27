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
        /* for test ... */
        System.out.println(DataBase.findAll());
        return dto;
    }
}
