package webserver.mapper;

import model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    default User MapToEntity(Map<String, String> map){
        return User.
                builder().
                userId(map.get("userId")).
                password(map.get("password")).
                name(map.get("name")).
                email(map.get("email")).
                build();
    }
}
