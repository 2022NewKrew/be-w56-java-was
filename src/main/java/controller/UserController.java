package controller;

import db.DataBase;
import model.RequestHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResponseStatus;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import static controller.RequestPathMapper.*;
import static java.net.URLDecoder.decode;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String ROOT = "/index.html";

    protected static void userCreatePath(RequestHeader requestHeader, Map<String, String> requestBody, DataOutputStream dos) {
        User user = new User(requestBody.get("userId"), requestBody.get("password"), requestBody.get("name"), requestBody.get("email"));
        DataBase.addUser(user);

        log.info("Added User : {}", DataBase.findUserById(requestBody.get("userId")).toString());
        response302Header(requestHeader.getContentType(), ROOT, dos);
    }

    protected static void userLoginPath(RequestHeader requestHeader, Map<String, String> requestBody, DataOutputStream dos) throws IOException {
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        User user = DataBase.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            log.info("Log-in failed : User not Found");
            response302Header(requestHeader.getContentType(), "/user/login_failed.html", false, dos);
            return;
        }
        log.info("Log-in Success");
        response302Header(requestHeader.getContentType(), ROOT, true, dos);
    }

    protected static void userListPath(RequestHeader requestHeader, DataOutputStream dos) throws IOException {
        byte[] body = userListFile().getBytes(StandardCharsets.UTF_8);
        responseHeader(ResponseStatus.OK, requestHeader.getContentType(), dos, body.length);
        responseBody(dos, body);
    }

    private static String userListFile() throws IOException {
        final String USERLIST_FORMAT = "" +
                "                <tr>\n" +
                "                    <th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>";

        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(LOCAL_PREFIX + "/user/list.html"));

        String line;
        while (!(line = br.readLine()).endsWith("<tbody>")) {
            sb.append(line);
        }
        sb.append(br.readLine());

        int idx = 1;
        for (User user : users) {
            String userId = decode(user.getUserId(), StandardCharsets.UTF_8);
            String userName = decode(user.getName(), StandardCharsets.UTF_8);
            String userEmail = decode(user.getEmail(), StandardCharsets.UTF_8);
            sb.append(String.format(USERLIST_FORMAT, idx++, userId, userName, userEmail));
        }

        while (!(line = br.readLine()).equals("")) {
            sb.append(line);
        }
        return sb.toString();
    }
}
