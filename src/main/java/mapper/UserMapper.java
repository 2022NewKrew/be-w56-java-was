package mapper;

import dto.UserDto;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import model.User;
import org.bson.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToDto(User user);

    List<UserDto> usersToDtos(List<User> users);

    List<User> documentsToUsers(List<Document> documents);

    default Document userToDocument(User user) {
        if(user == null) {
            return null;
        }
        Document document = new Document();
        document.put("userId", user.getUserId());
        document.put("password", user.getPassword());
        document.put("name", user.getName());
        document.put("email", user.getEmail());
        document.put("createTime", user.getCreateTime());
        document.put("modifiedTime", user.getModifiedTime());
        return document;
    }

    default User documentToUser(Document document) {
        if(document == null) {
            return null;
        }
        return new User(
                document.getString("userId"),
                document.getString("password"),
                document.getString("name"),
                document.getString("email"),
                Instant.ofEpochMilli(document.getDate("createTime").getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                Instant.ofEpochMilli(document.getDate("modifiedTime").getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}
