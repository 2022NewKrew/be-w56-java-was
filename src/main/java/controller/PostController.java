package controller;

import db.DataBase;
import domain.HttpBody;
import domain.HttpRequest;
import domain.HttpResponse;
import domain.RequestLine;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class PostController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Override
    public void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException {
        HttpRequest httpRequest = new HttpRequest(requestLine, HttpRequestUtils.parseHeaders(bufferedReader));
        HttpResponse httpResponse = new HttpResponse();
        String requestUrl = httpRequest.getRequestPath();
        String requestPath = HttpRequestUtils.getRequestPath(requestUrl);

        String httpBodyString = IOUtils.readData(bufferedReader, httpRequest.getContentLength());
        httpRequest.addHttpBody(HttpRequestUtils.parseQueryString(httpBodyString));

        switch (requestPath) {
            case "/user/create":
                HttpBody httpBody = httpRequest.getHttpBody();
                User user = new User(httpBody.get("userId"), httpBody.get("password"), httpBody.get("name"), httpBody.get("email"));
                DataBase.addUser(user);
                log.info("FindUser : {}", DataBase.findUserById(httpBody.get("userId")));
                httpResponse.response302(dos, "/index.html");
                break;
        }
    }
}
