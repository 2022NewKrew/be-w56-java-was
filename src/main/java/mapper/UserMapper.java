package mapper;

import domain.Email;
import domain.Name;
import domain.Password;
import domain.UserId;
import dto.UserDto;
import entity.UserEntity;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserEntity toUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        String userId = userDto.getUserId().toString();
        String password = userDto.getPassword().toString();
        String name = userDto.getName().toString();
        String email = userDto.getEmail().toString();
        LocalDateTime createdAt = userDto.getCreatedAt();
        return new UserEntity(userId, password, name, email, createdAt);
    }

    public static UserDto toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        UserId userId = new UserId(userEntity.getUserId());
        Password password = new Password(userEntity.getPassword());
        Name name = new Name(userEntity.getName());
        Email email = new Email(userEntity.getEmail());
        return new UserDto(userId, password, name, email, userEntity.getCreatedAt());
    }
}
