package app.service;

import java.util.concurrent.atomic.AtomicInteger;

import app.domain.User;
import app.repository.UserRepository;

public class UserService {
    private static UserService instance = new UserService();

    private final UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        return instance;
    }

    public void registerUser(User user) {
        userRepository.saveUser(user);
    }

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public String getUserListHtml() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger index = new AtomicInteger(1);

        userRepository.getUserList().stream()
                      .map(user -> "<tr><th scope='row'>" + index.getAndIncrement()
                              + "</th><td>" + user.getUserId() + "</td>"
                              + "<td>" + user.getName() + "</td>"
                              + "<td>" + user.getEmail() + "</td>"
                              + "<td><a href='#' class='btn btn-success' role='button'>수정</a></td></tr>")
                      .forEach(str -> sb.append(str + "\n"));

        return sb.toString();
    }
}
