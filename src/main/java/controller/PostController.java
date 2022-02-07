package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import collections.ResponseHeaders;
import dto.Response;
import service.UserService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        var temp = new HashMap<String, String>();
        temp.put("Location", "/");
        Response response = new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);

        userService.signUp(requestBody.getBodies());

        return response;
    }

    public Response userLogin(RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws IOException {
        var temp = new HashMap<String, String>();
        if (userService.signIn(requestBody.getBodies())) {
            temp.put("Location", "/");
            temp.put("Set-Cookie", "logined=true; Path=/");
            return new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);
        }

        temp.put("Set-Cookie", "logined=false; Path=/");
        temp.put("Location", "/user/login_failed.html");
        return new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);
    }

    public Response post(RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        var temp = new HashMap<String, String>();
        temp.put("Location", "/");
        Response response = new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);

        userService.post(requestBody.getBodies());

        return response;
    }

}
