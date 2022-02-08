package cafe.service;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.model.User;
import cafe.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService {

    private static final String WEBAPP_PATH = "/webapp";

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public String getUserListHtml() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(WEBAPP_PATH + "/user/list.html");
        byte[] bytes = resourceAsStream.readAllBytes();
        String htmlString = new String(bytes);

        StringBuilder usersHtml = new StringBuilder();

        AtomicInteger index = new AtomicInteger(1);
        userRepository.findAll().stream()
                .forEach(user -> {
                    usersHtml.append("<tr>");
                    usersHtml.append("<th scope=\"row\">").append(index.get()).append("</th>");
                    usersHtml.append("<td>").append(user.getUserId()).append("</td>");
                    usersHtml.append("<td>").append(user.getName()).append("</td>");
                    usersHtml.append("<td>").append(user.getEmail()).append("</td>");
                    usersHtml.append("<td><a class=\"btn btn-success\" href=\"#\" role=\"button\">수정</a></td>");
                    usersHtml.append("</tr>");
                    index.getAndIncrement();
                });
        return htmlString.replace("{{userList}}", usersHtml.toString());
    }

    public void createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        userRepository.addUser(user);
    }

    public void authenticateUser(String userId, String password) throws IncorrectLoginUserException {
        User user = userRepository.findUserById(userId);

        if (user == null || !user.isValidPassword(password)) {
            throw new IncorrectLoginUserException();
        }
    }
}
