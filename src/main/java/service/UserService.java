package service;

import db.RepositoryUserDbImpl;
import model.Request;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService {

    private final RepositoryUserDbImpl repositoryUserDb;

    public UserService() {
        this.repositoryUserDb = new RepositoryUserDbImpl();
    }

    public void save(Request request) throws SQLException {
        repositoryUserDb.save(request);
    }

    public boolean isRightLogin(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();
        User newUser = User.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .build();
        User findUser = repositoryUserDb.findUserById(queryString.get("userId"));

        return newUser.equals(findUser);
    }

    public boolean isLoginState(Request request) {
        String cookieValue = request.getCookies().get("logined");
        String opBool = Optional.
                ofNullable(cookieValue).
                orElse("false");
        return opBool.equals("true");
    }

    public byte[] userListToByte() throws IOException, SQLException {
        byte[] htmlBytes = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
        String htmlString = new String(htmlBytes);

        List<User> userList = repositoryUserDb.findAll();
        StringBuilder sb = new StringBuilder();

        for (User user : userList) {
            sb.append("<tr>");
            sb.append("<td> " + user.getUserId() + " </td>");
            sb.append("<td> " + user.getName() + " </td>");
            sb.append("<td> " + user.getEmail() + " </td>");
            sb.append("</tr>");
        }

        htmlString = htmlString.replace("{{userList}}", sb.toString());
        return htmlString.getBytes();
    }
}
