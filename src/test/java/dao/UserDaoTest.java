package dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UserDao 테스트")
class UserDaoTest {

    @DisplayName("입력받은 userId와 동일한 userId를 가지는 User를 Optional에 담아서 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "testUserId1,testPassword1,testName1,testEmail1",
            "testUserId2,testPassword2,testName2,testEmail2",
            "testUserId3,testPassword3,testName3,testEmail3"
    })
    void find(String userId, String password, String name, String email) {
        //give
        UserDao dao = UserDao.getInstance();
        User testUser = new User(userId, password, name, email);
        ObjectId id = dao.save(testUser);

        //when
        User user = dao.find(id);

        //then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);

        dao.delete(id);
    }

    @DisplayName("모든 User의 목록을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10, 100, 1000})
    void testFind(int numberOfUser) {
        //give
        UserDao dao = UserDao.getInstance();
        int baseSize = dao.find().size();

        List<ObjectId> ids = new ArrayList<>();
        for (int i = 0; i < numberOfUser; i++) {
            String userId = "userId" + i;
            String password = "password" + i;
            String name = "name" + i;
            String email = "email" + i;
            User user = new User(userId, password, name, email);
            ids.add(dao.save(user));
        }

        //when
        List<User> users = dao.find();

        //then
        assertThat(users.size()).isEqualTo(numberOfUser + baseSize);

        for (ObjectId id : ids) {
            dao.delete(id);
        }
    }

    @DisplayName("입력받은 User를 데이터베이스에 삽입한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "testUserId1,testPassword1,testName1,testEmail1",
            "testUserId2,testPassword2,testName2,testEmail2",
            "testUserId3,testPassword3,testName3,testEmail3"
    })
    void save(String userId, String password, String name, String email) {
        //give
        UserDao dao = UserDao.getInstance();

        User testUser = new User(userId, password, name, email);

        //when
        ObjectId id = dao.save(testUser);
        User savedUser = dao.find(id);

        //then
        assertThat(savedUser.getUserId()).isEqualTo(userId);
        assertThat(savedUser.getPassword()).isEqualTo(password);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getEmail()).isEqualTo(email);

        dao.delete(id);
    }

    @DisplayName("입력받은 User의 UserId와 동일한 User의 값을 수정한다.")
    @Test
    void update() {
        //give
        UserDao dao = UserDao.getInstance();
        User user = new User("testUserId", "testPassword", "testName", "testEmail");
        ObjectId id = dao.save(user);

        String newPassword = "newPassword";
        String newName = "newName";
        String newEmail = "newEmail";

        //when
        dao.update(new User(id, user.getUserId(), newPassword, newName, newEmail));
        User updateUser = dao.find(id);

        //then
        assertThat(updateUser.getPassword()).isEqualTo(newPassword);
        assertThat(updateUser.getName()).isEqualTo(newName);
        assertThat(updateUser.getEmail()).isEqualTo(newEmail);

        dao.delete(id);
    }

    @DisplayName("입력받은 User와 동일한 userId를 갖는 User를 데이터베이스에서 삭제한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "testUserId1,testPassword1,testName1,testEmail1",
            "testUserId2,testPassword2,testName2,testEmail2",
            "testUserId3,testPassword3,testName3,testEmail3"
    })
    void delete(String userId, String password, String name, String email) {
        //give
        UserDao dao = UserDao.getInstance();
        User testUser = new User(userId, password, name, email);
        ObjectId id = dao.save(testUser);
        assertThat(dao.find(id).getUserId()).isEqualTo(userId);

        //when
        dao.delete(id);

        //then
        assertThat(dao.find(id)).isNull();
    }
}
