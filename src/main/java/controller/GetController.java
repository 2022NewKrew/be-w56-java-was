package controller;

import collections.RequestHeaders;
import collections.RequestStartLine;
import dto.Response;
import collections.ResponseHeaders;
import service.UserService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;

public class GetController implements Controller {

    private final UserService userService;

    public GetController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doResponse(String methodName, DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 메서드 실행
        Class<GetController> targetClass = GetController.class;
        Method method = targetClass.getMethod(methodName, RequestStartLine.class, RequestHeaders.class);
        Response response = (Response) method.invoke(this, requestStartLine, requestHeaders);

        // 전송
        response200Header(dos, response.getHeaders());
        responseBody(dos, response.getBody());
    }

    public Response userCreate(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        // Response 만들기
        byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());
        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("Accept"));
        Response response = new Response(new ResponseHeaders(temp), body);

        userService.signUp(requestStartLine.getParameters());

        return response;
    }

    public Response staticResource(RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + requestStartLine.getPath()).toPath());
        var temp = new HashMap<String, String>();
        temp.put("Content-Length", String.valueOf(body.length));
        temp.put("Content-Type", requestHeaders.getHeader("Accept"));
        Response response = new Response(new ResponseHeaders(temp), body);

        return response;
    }

    // TODO: 추후 선택한 response header 전송하도록 수정하기
    private void response200Header(DataOutputStream dos, ResponseHeaders responseHeaders) {
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
//            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", responseHeaders.getHeader("Content-Type")));
            dos.writeBytes(String.format("Content-Length: %s\r\n", responseHeaders.getHeader("Content-Length")));
//            dos.writeBytes(String.format("Location: %s\r\n", "/"));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
