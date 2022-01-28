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
    private byte[] outputBody = new byte[0];
    private final String INDEX_PATH = "/index.html";
    private final String ROOT_DIRECTORY = "./webapp";

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
            File file = new File(ROOT_DIRECTORY+INDEX_PATH);
            outputBody = Files.readAllBytes(file.toPath());
            HttpResponseUtils.writeStatusCode(dos, 302);
            HttpResponseUtils.writeLocation(dos, INDEX_PATH);
            HttpResponseUtils.writeCookie(dos, "logined", "true", "/");
            HttpResponseUtils.writeBody(dos, outputBody);
            return;
        }
        String path = ROOT_DIRECTORY+"/user/login_failed.html";
        File file = new File(path);
        outputBody = Files.readAllBytes(file.toPath());
        HttpResponseUtils.writeStatusCode(dos, 302);
        HttpResponseUtils.writeLocation(dos, "/user/login_failed.html");
        HttpResponseUtils.writeCookie(dos, "logined", "false", "/");
        HttpResponseUtils.writeBody(dos, outputBody);
    }
}
