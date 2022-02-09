package service;

import db.DataBase;
import dto.RequestInfo;
import exception.UnAuthorizedException;
import model.User;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class UserService {

    private static final UserService INSTANCE;

    static {
        INSTANCE = new UserService();
    }

    private UserService() {}

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void createUser(Map<String, String> queryParams) {
        User newUser = User.builder()
                .userId(queryParams.get("userId"))
                .password(queryParams.get("password"))
                .name(queryParams.get("name"))
                .email(queryParams.get("email"))
                .build();

        DataBase.addUser(newUser);
    }

    public void loginUser(Map<String, String> bodyParams) {
        String userId = bodyParams.get("userId");
        String password = bodyParams.get("password");
        User user = Optional.ofNullable(DataBase.findUserById(userId))
                            .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if(!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
    }

    public void checkAuthorization(RequestInfo requestInfo) {
        Map<String, String> cookies = requestInfo.getCookies();
        boolean loggedIn = Boolean.parseBoolean(cookies.getOrDefault("logged_in", "false"));

        if(!loggedIn) {
            throw new UnAuthorizedException();
        }
    }

    /*
    <tr>
        <th scope="row">1</th> <td>javajigi</td> <td>자바지기</td> <td>javajigi@sample.net</td><td><a href="#" class="btn btn-success" role="button">수정</a></td>
        </tr>
        <tr>
        <th scope="row">2</th> <td>slipp</td> <td>슬립</td> <td>slipp@sample.net</td><td><a href="#" class="btn btn-success" role="button">수정</a></td>
    </tr>
     */
    public String getUserListTableTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append("<tbody>");
        List<User> userList = new ArrayList<>(DataBase.findAll());


        for (int idx = 1; idx <= userList.size(); ++idx) {
            User user = userList.get(idx - 1);
            sb
                    .append("<tr>")
                    .append("<th scope=\"row\"")
                    .append(idx)
                    .append("</th><td>")
                    .append(user.getUserId())
                    .append("</td><td>")
                    .append(user.getName())
                    .append("</td><td>")
                    .append(user.getEmail())
                    .append("</td><td>")
                    .append("<a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
        }

        return sb.toString();
    }
}
