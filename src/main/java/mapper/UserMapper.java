package mapper;

import dto.UserDto;
import model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper instance = Mappers.getMapper(UserMapper.class);

    UserDto userToDto(User user);
}
