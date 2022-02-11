package controller;

import collections.RequestHeaders;
import collections.RequestStartLine;
import http.HttpResponse;
import collections.ResponseHeaders;
import dto.ResponseBodyDto;
import http.HttpRequest;
import model.Memo;
import model.User;
import org.apache.tika.Tika;
import service.MemoService;
import service.UserService;
import util.HttpRequestUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetController implements Controller {

    private final UserService userService;
    private final MemoService memoService;
    private static final Tika tika = new Tika();

    public GetController(UserService userService, MemoService memoService) {
        this.userService = userService;
        this.memoService = memoService;
    }

    @Override
    public void doResponse(String methodName, DataOutputStream dos, HttpRequest httpRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // HttpRequest 구조 분해
        RequestStartLine requestStartLine = httpRequest.getRequestStartLine();
        RequestHeaders requestHeaders = httpRequest.getRequestHeaders();

        // 메서드 실행
        Class<GetController> targetClass = GetController.class;
        Method method = targetClass.getMethod(methodName, RequestStartLine.class, RequestHeaders.class);
        HttpResponse httpResponse = (HttpResponse) method.invoke(this, requestStartLine, requestHeaders);

        // 전송
        responseStatusLine(dos, httpResponse.getStatusLine());
        responseHeader(dos, httpResponse.getHeaders());
        responseBody(dos, httpResponse.getBody());
    }

    public HttpResponse userCreate(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        var headers = new HashMap<String, String>();
        headers.put("Location", "/");

        userService.signUp(requestStartLine.getParameters());

        return HttpResponse.create302RedirectHttpResponse(headers);
    }

    public HttpResponse staticResource(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        ResponseBodyDto responseBodyDto = HTML_VIEW.staticResourceView(requestStartLine.getPath());
        byte[] body = responseBodyDto.getBody();

        var headers = new HashMap<String, String>();
        headers.put("Content-Length", String.valueOf(body.length));
        headers.put("Content-Type", responseBodyDto.getContentType());

        return HttpResponse.create200ForwordHttpResponse(headers, body);
    }

    public HttpResponse userList(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        String cookieString = requestHeaders.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieString);
        String logined = cookies.get("logined");

        if (logined.equals("true")) {
            // 사용자 목록 로직
            Collection<User> users = userService.userList();
            byte[] body = HTML_VIEW.userListView(users);

            var headers = new HashMap<String, String>();
            headers.put("Content-Length", String.valueOf(body.length));
            headers.put("Content-Type", "text/html; charset=utf-8");

            return HttpResponse.create200ForwordHttpResponse(headers, body);
        }

        // 로그인 화면으로 redirect
        var headers = new HashMap<String, String>();
        headers.put("Location", "/user/login.html");

        return HttpResponse.create302RedirectHttpResponse(headers);
    }

    public HttpResponse index(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        List<Memo> memos = memoService.postList();
        byte[] body = HTML_VIEW.indexView(memos);

        var headers = new HashMap<String, String>();
        headers.put("Content-Length", String.valueOf(body.length));
        headers.put("Content-Type", "text/html; charset=utf-8");

        return HttpResponse.create200ForwordHttpResponse(headers, body);
    }

    public HttpResponse userLogout(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        var temp = new HashMap<String, String>();
        temp.put("Set-Cookie", "logined=false; Path=/");
        temp.put("Location", "/");
        return new HttpResponse("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);
    }

    public HttpResponse post(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        String cookieString = requestHeaders.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieString);
        String logined = cookies.get("logined");

        var headers = new HashMap<String, String>();
        if (logined.equals("true")) {
            ResponseBodyDto responseBodyDto = HTML_VIEW.staticResourceView("/qna/form.html");
            byte[] body = responseBodyDto.getBody();

            headers.put("Content-Length", String.valueOf(body.length));
            headers.put("Content-Type", "text/html; charset=utf-8");

            return HttpResponse.create200ForwordHttpResponse(headers, body);
        }

        // 로그인 화면으로 redirect
        headers.put("Location", "/user/login.html");

        return HttpResponse.create302RedirectHttpResponse(headers);
    }

}
