package user.controller;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class UserCreate {
    public String execute(BufferedReader br, Map<String, String> headers) throws IOException {
        String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        return "index.html";
    }
}
