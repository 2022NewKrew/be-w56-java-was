package webserver.Controller;

import db.DataBase;
import model.RequestHeader;
import model.User;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;


public class UserController {
    private static final HttpResponseUtils httpResponseUtils = new HttpResponseUtils();
    byte[] outputBody = new byte[0];

    public void signUp(DataOutputStream dos, RequestHeader header) throws IOException {
        Map<String, String> body = header.getBody();
        String userId = body.get("userId");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        String path = "./webapp/index.html";
        File file = new File(path);
        outputBody = Files.readAllBytes(file.toPath());
        httpResponseUtils.response300Header(dos, outputBody.length);
        HttpResponseUtils.responseBody(dos, outputBody);
    }
}
