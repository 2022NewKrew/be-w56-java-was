package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import collections.ResponseHeaders;
import dto.Response;
import service.UserService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;

public class PostController implements Controller {

    private final UserService userService;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doResponse(String methodName, DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 메서드 실행
        Class<PostController> targetClass = PostController.class;
        Method method = targetClass.getMethod(methodName, RequestStartLine.class, RequestHeaders.class, RequestBody.class);
        Response response = (Response) method.invoke(this, requestStartLine, requestHeaders, requestBody);

        // 전송
        responseStatusLine(dos, response.getStatusLine());
        responseHeader(dos, response.getHeaders());
        responseBody(dos, response.getBody());
    }

    public Response userCreate(RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());
        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
        temp.put("Location", "/index.html");
        Response response = new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), body);

        userService.signUp(requestBody.getBodies());

        return response;
    }

}
