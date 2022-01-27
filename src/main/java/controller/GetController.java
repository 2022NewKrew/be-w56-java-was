package controller;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import dto.Response;
import collections.ResponseHeaders;
import org.apache.tika.Tika;
import service.UserService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class GetController implements Controller {

    private final UserService userService;

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
        byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());
        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("text/html; charset=utf-8"));
        temp.put("Location", "/index.html");
        Response response = new Response("HTTP/1.1 302 Found", new ResponseHeaders(temp), body);

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
            Tika tika = new Tika();
            contentType = tika.detect(file);
        }
        temp.put("Content-Type", contentType);

        Response response = new Response("HTTP/1.1 200 OK", new ResponseHeaders(temp), body);
        return response;
    }

}
