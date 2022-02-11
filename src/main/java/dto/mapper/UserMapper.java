package dto.mapper;

import dto.UserCreateDto;
import dto.UserResponseDto;
import model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface UserMapper{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntityFromSaveDto(UserCreateDto userCreateDto);

    List<UserResponseDto> toDtoList(List<User> users);

    UserCookieDto toCookieDto(User user);
}
