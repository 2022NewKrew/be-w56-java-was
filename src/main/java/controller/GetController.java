package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import dto.Response;
import collections.ResponseHeaders;
import model.User;
import org.apache.tika.Tika;
import service.UserService;
import util.HttpRequestUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
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
        temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
        temp.put("Location", "/");
        Response response = new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);

        userService.signUp(requestStartLine.getParameters());

        return response;
    }

    public Response staticResource(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        File file = new File("./webapp" + requestStartLine.getPath());
        Path filePath = file.toPath();

        byte[] body = Files.readAllBytes(filePath);
        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        String contentType = Files.probeContentType(filePath);
        if (contentType == null || contentType.equals("")) {
            contentType = tika.detect(file);
        }
        temp.put("Content-Type", contentType);

        return new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
    }

    public Response userList(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        String cookieString = requestHeaders.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieString);
        String logined = cookies.get("logined");

        if (logined.equals("true")) {
            // 사용자 목록 로직
            Collection<User> users = userService.list();
            File file = new File("./webapp" + "/user/list.html");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            makeListView(users, br, line, sb);
            byte[] body = sb.toString().getBytes();

            var temp = new HashMap<String, String>();
            temp.put("Content-Length", String.valueOf(body.length));
            temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
            return new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
        }

        // 로그인 화면으로 redirect
        byte[] body = Files.readAllBytes(new File("./webapp" + "/user/login.html").toPath());

        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
        temp.put("Location", "/user/login.html");
        return new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), body);
    }

    public Response index(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());

        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
        return new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
    }

    public Response userLogout(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        var temp = new HashMap<String, String>();
        temp.put("Set-Cookie", "logined=false; Path=/");
        temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
        temp.put("Location", "/");
        return new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), null);
    }

    private void makeListView(Collection<User> users, BufferedReader br, String line, StringBuilder sb) throws IOException {
        while (line != null) {
            sb.append(line).append("\r\n");
            if (line.contains("<tbody>")) {
                int cnt = 1;
                for (User user : users) {
                    sb.append("<tr>\r\n");
                    sb.append("<th scope=\"row\">").append(cnt).append("</th>\r\n");
                    sb.append("<td>").append(user.getUserId()).append("</td>\r\n");
                    sb.append("<td>").append(user.getName()).append("</td>\r\n");
                    sb.append("<td>").append(user.getEmail()).append("</td>\r\n");
                    cnt++;
                }
            }
            line = br.readLine();
        }
    }

}
