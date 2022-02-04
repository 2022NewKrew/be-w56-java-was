package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import dto.Response;
import collections.ResponseHeaders;
import dto.ResponseBodyDto;
import model.User;
import org.apache.tika.Tika;
import service.UserService;
import util.HttpRequestUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GetController implements Controller {

    private final UserService userService;
    private static final Tika tika = new Tika();

    public GetController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doResponse(String methodName, DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 메서드 실행
        Class<GetController> targetClass = GetController.class;
        Method method = targetClass.getMethod(methodName, RequestStartLine.class, RequestHeaders.class);
        Response response = (Response) method.invoke(this, requestStartLine, requestHeaders);

        // 전송
        responseStatusLine(dos, response.getStatusLine());
        responseHeader(dos, response.getHeaders());
        responseBody(dos, response.getBody());
    }

    public Response userCreate(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        var temp = new HashMap<String, String>();
        temp.put("Location", "/");
        Response response = new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);

        userService.signUp(requestStartLine.getParameters());

        return response;
    }

    public Response staticResource(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        ResponseBodyDto responseBodyDto = HTML_VIEW.staticResourceView(requestStartLine.getPath());
        byte[] body = responseBodyDto.getBody();

        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", responseBodyDto.getContentType());

        return new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
    }

    public Response userList(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        String cookieString = requestHeaders.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieString);
        String logined = cookies.get("logined");

        if (logined.equals("true")) {
            // 사용자 목록 로직
            Collection<User> users = userService.list();
            byte[] body = HTML_VIEW.userListView(users);

            var temp = new HashMap<String, String>();
            temp.put("Content-Length", String.valueOf(body.length));
            temp.put("Content-Type", "text/html; charset=utf-8");
            return new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
        }

        // 로그인 화면으로 redirect
        ResponseBodyDto responseBodyDto = HTML_VIEW.staticResourceView("/user/login.html");
        byte[] body = responseBodyDto.getBody();

        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Location", "/user/login.html");
        return new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), body);
    }

    public Response index(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        ResponseBodyDto responseBodyDto = HTML_VIEW.staticResourceView("/index.html");
        byte[] body = responseBodyDto.getBody();

        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", responseBodyDto.getContentType());
        return new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
    }

    public Response userLogout(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        var temp = new HashMap<String, String>();
        temp.put("Set-Cookie", "logined=false; Path=/");
        temp.put("Location", "/");
        return new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);
    }

}
