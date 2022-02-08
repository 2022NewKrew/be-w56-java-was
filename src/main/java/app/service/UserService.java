import java.util.concurrent.atomic.AtomicInteger;

import app.db.DataBase;
import app.model.User;

public class UserService {
    private static UserService instance = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return instance;
    }

    public void saveUser(User user) {
        DataBase.addUser(user);
    }

    public User getUser(String userId) {
        return DataBase.findUserById(userId);
    }

    public String getUserListHtml() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger index = new AtomicInteger(1);

        DataBase.findAll().stream()
                .map(user -> "<tr><th scope='row'>" + index.getAndIncrement()
                        + "</th><td>" + user.getUserId() + "</td>"
                        + "<td>" + user.getName() + "</td>"
                        + "<td>" + user.getEmail() + "</td>"
                        + "<td><a href='#' class='btn btn-success' role='button'>수정</a></td></tr>")
                .forEach(str -> sb.append(str + "\n"));

        return sb.toString();
    }
}
