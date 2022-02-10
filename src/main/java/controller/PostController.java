package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import collections.ResponseHeaders;
import http.HttpResponse;
import http.HttpRequest;
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
    public void doResponse(String methodName, DataOutputStream dos, HttpRequest httpRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // HttpRequest 구조 분해
        RequestStartLine requestStartLine = httpRequest.getRequestStartLine();
        RequestHeaders requestHeaders = httpRequest.getRequestHeaders();
        RequestBody requestBody = httpRequest.getRequestBody();

        // 메서드 실행
        Class<PostController> targetClass = PostController.class;
        Method method = targetClass.getMethod(methodName, RequestStartLine.class, RequestHeaders.class, RequestBody.class);
        HttpResponse httpResponse = (HttpResponse) method.invoke(this, requestStartLine, requestHeaders, requestBody);

        // 전송
        responseStatusLine(dos, httpResponse.getStatusLine());
        responseHeader(dos, httpResponse.getHeaders());
        responseBody(dos, httpResponse.getBody());
    }

    public HttpResponse userCreate(RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws IOException {
        var headers = new HashMap<String, String>();
        headers.put("Location", "/");

        userService.signUp(requestBody.getBodies());

        return HttpResponse.create302RedirectHttpResponse(headers);
    }

    public HttpResponse userLogin(RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) throws IOException {
        var headers = new HashMap<String, String>();
        if (userService.signIn(requestBody.getBodies())) {
            headers.put("Set-Cookie", "logined=true; Path=/");
            headers.put("Location", "/");

            return HttpResponse.create302RedirectHttpResponse(headers);
        }

        headers.put("Set-Cookie", "logined=false; Path=/");
        headers.put("Location", "/user/login_failed.html");

        return HttpResponse.create302RedirectHttpResponse(headers);
    }

    public HttpResponse post(RequestStartLine requestStartLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        var headers = new HashMap<String, String>();
        headers.put("Location", "/");

        userService.post(requestBody.getBodies());

        return HttpResponse.create302RedirectHttpResponse(headers);
    }

}
