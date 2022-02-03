package cafe.service;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.db.DataBase;
import cafe.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService {

    private static final String WEBAPP_PATH = "./webapp";

    public String getUserListHtml() throws IOException {
        File file = new File(WEBAPP_PATH + "/user/list.html");
        byte[] bytes = Files.readAllBytes(file.toPath());
        String htmlString = new String(bytes);

        StringBuilder usersHtml = new StringBuilder();

        AtomicInteger index = new AtomicInteger(1);
        DataBase.findAll().stream()
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
        DataBase.addUser(user);
    }

    public void authenticateUser(String userId, String password) throws IncorrectLoginUserException {
        User user = DataBase.findUserById(userId);

        if (user == null || !user.isValidPassword(password)) {
            throw new IncorrectLoginUserException();
        }
    }
}
