package user.controller;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserLogin {
    public Map<String, String> execute(BufferedReader br, Map<String, String> headers) throws IOException {
        String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        User user = DataBase.getUser(params.get("userId"));
        Map<String, String> loginResult = new HashMap<String, String>();
        if (user != null && user.getPassword().equals(params.get("password"))) {
            loginResult.put("path", "index.html");
            loginResult.put("cookie", "logined=true");
        }
        else {
            loginResult.put("path", "user/login_failed.html");
            loginResult.put("cookie", "logined=false");
        }
        return loginResult;
    }
}
