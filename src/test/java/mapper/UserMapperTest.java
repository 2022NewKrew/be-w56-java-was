package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import dto.UserDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("UserMapper 테스트")
class UserMapperTest {

    @DisplayName("User to UserDto 테스트")
    @Test
    void userToDto() {
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("userId", "password", "name", "email");
        UserDto testDto = userMapper.userToDto(user);

        assertThat(testDto.getUserId()).isEqualTo(user.getUserId());
        assertThat(testDto.getName()).isEqualTo(user.getName());
        assertThat(testDto.getEmail()).isEqualTo(user.getEmail());
    }

    @ParameterizedTest
    @MethodSource("getUsers")
    void usersToDtos(List<User> users) {
        UserMapper userMapper = UserMapper.INSTANCE;
        List<UserDto> userDtos = userMapper.usersToDtos(users);

        User firstUser = users.get(0);
        UserDto firstDto = userDtos.get(0);

        assertThat(firstUser.getUserId()).isEqualTo(firstDto.getUserId());
        assertThat(users.size()).isEqualTo(userDtos.size());
    }

    private static Stream<List<User>> getUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            users.add(
                    new User(
                            "userId" + i,
                            "password" + i,
                            "name" + i,
                            "email" + i));
        }

        return Stream.of(users);
    }

    @ParameterizedTest
    @MethodSource("getDocuments")
    void documentsToUsers(List<Document> documents) {
        UserMapper userMapper = UserMapper.INSTANCE;
        List<User> users = userMapper.documentsToUsers(documents);

        Document firstDocument = documents.get(0);
        User firstUser = users.get(0);

        assertThat(documents.size()).isEqualTo(users.size());
        assertThat(firstDocument.get("userId")).isEqualTo(firstUser.getUserId());
        assertThat(firstDocument.get("password")).isEqualTo(firstUser.getPassword());
        assertThat(firstDocument.get("name")).isEqualTo(firstUser.getName());
        assertThat(firstDocument.get("email")).isEqualTo(firstUser.getEmail());
    }

    private static Stream<List<Document>> getDocuments() {
        List<Document> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            document.put("_id", new ObjectId());
            document.put("userId", "userId" + i);
            document.put("password", "password" + i);
            document.put("name", "name" + i);
            document.put("email", "email" + i);
            document.put("createTime", new Date());
            document.put("modifiedTime", new Date());
            result.add(document);
        }

        return Stream.of(result);
    }

    @Test
    void userToDocument() {
        UserMapper userMapper = UserMapper.INSTANCE;
        User user = new User("testUserId", "testPassword", "testName", "testEmail");
        Document document = userMapper.userToDocument(user);

        assertThat(document.get("userId")).isEqualTo(user.getUserId());
        assertThat(document.get("password")).isEqualTo(user.getPassword());
        assertThat(document.get("name")).isEqualTo(user.getName());
        assertThat(document.get("email")).isEqualTo(user.getEmail());
    }

    @Test
    void documentToUser() {
        UserMapper userMapper = UserMapper.INSTANCE;
        Document document = new Document();
        document.put("_id", new ObjectId());
        document.put("userId", "testUserId");
        document.put("password", "testPassword");
        document.put("name", "testName");
        document.put("email", "testEmail");
        document.put("createTime", new Date());
        document.put("modifiedTime", new Date());
        User user = userMapper.documentToUser(document);

        assertThat(user.getUserId()).isEqualTo(document.get("userId"));
        assertThat(user.getPassword()).isEqualTo(document.get("password"));
        assertThat(user.getName()).isEqualTo(document.get("name"));
        assertThat(user.getEmail()).isEqualTo(document.get("email"));
    }
}
