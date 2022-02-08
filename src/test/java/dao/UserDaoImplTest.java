package dao;

import model.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

class UserDaoImplTest {
    private final String TEST_ID = "testid";
    private static UserDao userDao = new UserDaoImpl();

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        userDao.openConnection();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        userDao.closeConnection();
    }

    @AfterEach
    void deleteTestUser() throws SQLException {
        if (userDao.findByUserId(TEST_ID) != null)
            userDao.delete(TEST_ID);
    }

    @DisplayName("save 및 findByUserId 테스트")
    @Test
    void saveAndFindByUserIdTest() throws SQLException {
        User user = new User(TEST_ID, "passwd", "testname", "test@kakao.com");
        userDao.save(user);

        User foundUser = userDao.findByUserId(TEST_ID);
        Assertions.assertEquals("testname", foundUser.getName());
    }

    @DisplayName("User list 반환 테스트")
    @Test
    void findAllTest() throws SQLException {
        List<User> userList = userDao.findAll();
        int count = userList.size();

        User user = new User(TEST_ID, "passwd", "testname", "test@kakao.com");
        userDao.save(user);

        userList = userDao.findAll();
        Assertions.assertEquals(count+1, userList.size());
    }
}