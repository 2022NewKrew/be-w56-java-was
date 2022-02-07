package mapper;

import dto.UserDto;
import java.util.List;
import model.User;
import org.bson.Document;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper instance = Mappers.getMapper(UserMapper.class);

    UserDto userToDto(User user);

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
                document.getString("email")
        );
    }
}
