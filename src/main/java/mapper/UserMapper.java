package mapper;

import dto.UserDto;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    UserDto userToDto(User user);

    List<UserDto> usersToDtos(List<User> users);

    List<User> documentsToUsers(List<Document> documents);

    default Document userToDocument(User user) {
        if (user == null) {
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
        if (document == null) {
            return null;
        }
        return new User(
                document.getObjectId("_id"),
                document.getString("userId"),
                document.getString("password"),
                document.getString("name"),
                document.getString("email"),
                new Timestamp(document.getDate("createTime").getTime()).toLocalDateTime()
                        .minus(9, ChronoUnit.HOURS),
                new Timestamp(document.getDate("modifiedTime").getTime()).toLocalDateTime()
                        .minus(9, ChronoUnit.HOURS)
        );
    }

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id.toString();
    }
}
