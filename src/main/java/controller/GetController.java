package controller;

import db.DataBase;
import domain.HttpRequest;
import domain.HttpResponse;
import domain.RequestLine;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class GetController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(GetController.class);

    @Override
    public void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpRequest httpRequest = new HttpRequest(requestLine, HttpRequestUtils.parseHeaders(bufferedReader));
        HttpResponse httpResponse = new HttpResponse();
        String requestUrl = httpRequest.getRequestPath();
        String requestPath = HttpRequestUtils.getRequestPath(requestUrl);

        byte[] body;

        switch (requestPath) {
            case "/create/user":
                String queryString = HttpRequestUtils.getQueryStringByUrl(requestUrl);
                Map<String, String> queryMap = HttpRequestUtils.parseQueryString(queryString);
                User user = new User(queryMap.get("userId"), queryMap.get("password"), queryMap.get("name"), queryMap.get("email"));
                DataBase.addUser(user);
                log.info("FindUser : {}", DataBase.findUserById(queryMap.get("userId")));
                body = DataBase.findUserById(queryMap.get("userId")).toString().getBytes(StandardCharsets.UTF_8);
                httpResponse.response200(dos, body, HttpRequestUtils.parseContentType(requestUrl));
                break;

            case "/":
                body = Files.readAllBytes(new File("./webapp", "/index.html").toPath());
                httpResponse.response200(dos, body, HttpRequestUtils.parseContentType(requestUrl));
                break;

            default:
                body = Files.readAllBytes(new File("./webapp", requestUrl).toPath());
                httpResponse.response200(dos, body, HttpRequestUtils.parseContentType(requestUrl));
                break;
        }
    }
}
