package webserver.Controller;

import db.DataBase;
import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.User;
import util.HttpRequestUtils;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
public class UserController {
    private byte[] outputBody = new byte[0];
    private final String INDEX_PATH = "/index.html";
    private final String STATIC_ROOT_PATH = "./webapp";

    public void signUp(DataOutputStream dos, RequestHeader header) throws IOException {
        Map<String, String> body = header.getBody();
        String userId = body.get("userId");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        File file = new File(ROOT_DIRECTORY+INDEX_PATH);
        outputBody = Files.readAllBytes(file.toPath());
        HttpResponseUtils.writeStatusCode(dos, 302);
        HttpResponseUtils.writeLocation(dos, INDEX_PATH);
        HttpResponseUtils.writeBody(dos, outputBody);
    }

    public void login(DataOutputStream dos, RequestHeader header) throws IOException {
        Map<String, String> body = header.getBody();
        User user = DataBase.findUserById(body.get("userId"));
        if (user != null && user.getPassword().equals(body.get("password"))) {
            File file = new File(STATIC_ROOT_PATH +INDEX_PATH);
            outputBody = Files.readAllBytes(file.toPath());
            HttpResponseUtils.writeStatusCode(dos, 302);
            HttpResponseUtils.writeLocation(dos, INDEX_PATH);
            HttpResponseUtils.writeCookie(dos, "logined", "true", "/");
            HttpResponseUtils.writeBody(dos, outputBody);
            return;
        }
        String path = STATIC_ROOT_PATH +"/user/login_failed.html";
        File file = new File(path);
        outputBody = Files.readAllBytes(file.toPath());
        HttpResponseUtils.writeStatusCode(dos, 302);
        HttpResponseUtils.writeLocation(dos, "/user/login_failed.html");
        HttpResponseUtils.writeCookie(dos, "logined", "false", "/");
        HttpResponseUtils.writeBody(dos, outputBody);
    }

    public void userList(DataOutputStream dos, RequestHeader header) throws IOException {
        Map<String, String> requestInfo = header.getRequestInfo();
        String token = requestInfo.getOrDefault("Cookie", "");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(token);
        if (cookies.getOrDefault("logined", "false").equals("true")) {
            File file = new File(STATIC_ROOT_PATH +"/user/list.html");
            outputBody = Files.readAllBytes(file.toPath());
            StringBuilder sb = new StringBuilder(new String(outputBody));
            StringBuilder newSb = new StringBuilder();
            int row = 0;
            for (User user :
                    DataBase.findAll()) {
                newSb.append("<tr>");
                newSb.append(String.format("<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td>",
                        ++row, user.getUserId(), user.getName(), user.getEmail()));
                newSb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
            }
            sb.insert(sb.lastIndexOf("<tbody>"), newSb);
            outputBody = sb.toString().getBytes(StandardCharsets.UTF_8);
            HttpResponseUtils.writeStatusCode(dos, 200);
            HttpResponseUtils.writeLocation(dos, INDEX_PATH);
            HttpResponseUtils.writeBody(dos, outputBody);
            return;
        }
        String path = STATIC_ROOT_PATH +"/user/login.html";
        File file = new File(path);
        outputBody = Files.readAllBytes(file.toPath());
        HttpResponseUtils.writeStatusCode(dos, 302);
        HttpResponseUtils.writeLocation(dos, "/user/login.html");
        HttpResponseUtils.writeBody(dos, outputBody);
    }
}
