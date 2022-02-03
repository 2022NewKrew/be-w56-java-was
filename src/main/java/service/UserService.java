package service;

import db.RepositoryUserDbImpl;
import model.Request;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserService {
    private UserService() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void save(Request request) throws SQLException {
        RepositoryUserDbImpl.save(request);
    }

    public static boolean isRightLogin(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();
        User newUser = User.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .build();
        User findUser = RepositoryUserDbImpl.findUserById(queryString.get("userId"));

        return newUser.equals(findUser);
    }

    public static boolean isLoginState(Request request) {
        return request.getCookies().get("logined").equals("true");
    }

    public static byte[] userListToByte() throws IOException, SQLException {
        byte[] htmlBytes = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
        String htmlString = new String(htmlBytes);

        List<User> userList = RepositoryUserDbImpl.findAll();
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
