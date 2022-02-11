package service;

import model.RequestHeader;
import model.User;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    private static class SignUpServiceHelper {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return SignUpServiceHelper.INSTANCE;
    }

    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }

    public boolean login(String userId, String password) throws SQLException {
        User user = userRepository.findOne(userId)
                .orElseThrow();
        if (user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }


    public void save(RequestHeader requestHeader) throws SQLException {
        userRepository.join(makeUser(requestHeader));
    }

    private User makeUser(RequestHeader requestHeader) {
        return User.builder()
                .userId(requestHeader.getParameter("userId"))
                .password(requestHeader.getParameter("password"))
                .name(requestHeader.getParameter("name"))
                .email(requestHeader.getParameter("email"))
                .build();
    }
}
