package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import httpmodel.HttpSession;
import httpmodel.HttpStatus;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import model.User;
import service.UserService;

public class UsersController extends AbstractController {

    private static final String USER = "user";

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpSession httpSession = httpRequest.getHttpSession();
        if (Objects.isNull(httpSession.getAttribute(USER))) {
            httpResponse.set302Found("/user/login.html");
            return;
        }
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(new FileInputStream("webapp/user/list.html")));
        String file = createHtml(bufferedReader);
        bufferedReader.close();

        httpResponse.set200OK(httpRequest,
            Objects.requireNonNull(file).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setErrorResponse("[ERROR] 지원하지 않는 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED);
    }

    private String createHtml(BufferedReader bufferedReader) throws IOException {
        StringJoiner stringJoiner = new StringJoiner("\r\n");
        String line = bufferedReader.readLine();
        while (Objects.nonNull(line)) {
            stringJoiner.add(line);
            putUserInfoIfLineEqualsTbody(line, stringJoiner);
            line = bufferedReader.readLine();
        }
        return stringJoiner.toString();
    }

    private void putUserInfoIfLineEqualsTbody(String line, StringJoiner stringJoiner) {
        if (!line.contains("<tbody>")) {
            return;
        }
        List<User> users = userService.findAll();
        int i = 1;
        for (User user : users) {
            stringJoiner.add("<tr>")
                .add("<th scope=\"row\">" +
                    i++ +
                    "</th> <td>" +
                    user.getUserId() +
                    "</td> <td>" +
                    user.getName() +
                    "</td> <td>" +
                    user.getEmail() +
                    "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                .add("<tr/>");
        }
    }
}
