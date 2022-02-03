package service;

import db.RepositoryDbImpl;
import db.RepositoryImpl;
import model.Request;
import model.User;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {
    private UserService() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void save(Request request) throws SQLException {
        RepositoryDbImpl.save(request);
    }

    public static boolean isRightLogin(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();
        User newUser = User.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .build();
        User findUser = RepositoryDbImpl.findUserById(queryString.get("userId"));

        return newUser.equals(findUser);
    }

    public static boolean isLoginState(Request request) {
        return request.getCookies().get("logined").equals("true");
    }

    public static byte[] userListToFile() throws IOException, SQLException {
        List<User> userList = RepositoryDbImpl.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<style>\n" +
                "  table {\n" +
                "    width: 80%;\n" +
                "    border: 1px solid #444444;\n" +
                "    border-collapse: collapse;\n" +
                "  }\n" +
                "  th, td {\n" +
                "    border: 1px solid #444444;\n" +
                "    padding: 10px;\n" +
                "  }\n" +
                "</style>");
        sb.append("</head>");
        sb.append("<table>");
        sb.append("<th> userId </th>");
        sb.append("<th> name </th>");
        sb.append("<th> email </th>");

        for (User user : userList) {
            sb.append("<tr>");
            sb.append("<td> " + user.getUserId() + " </td>");
            sb.append("<td> " + user.getName() + " </td>");
            sb.append("<td> " + user.getEmail() + " </td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");
        return String.valueOf(sb).getBytes();
    }
}
