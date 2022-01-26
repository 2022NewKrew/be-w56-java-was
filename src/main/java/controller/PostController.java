package controller;

import collections.RequestHeaders;
import collections.RequestStartLine;
import collections.ResponseHeaders;
import dto.Response;
import service.UserService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostController implements Controller {

    private final UserService userService;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doResponse(String methodName, DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 메서드 실행
        Class<PostController> targetClass = PostController.class;
        Method method = targetClass.getMethod(methodName, RequestStartLine.class, RequestHeaders.class);
        Response response = (Response) method.invoke(this, requestStartLine, requestHeaders);

        // 전송
        response200Header(dos, response.getHeaders());
        responseBody(dos, response.getBody());
    }

    public String userCreate(DataOutputStream dos, RequestStartLine requestStartLine, RequestHeaders requestHeaders) {
        return "";
    }

    // TODO: 추후 선택한 response header 전송하도록 수정하기
    private void response200Header(DataOutputStream dos, ResponseHeaders responseHeaders) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", responseHeaders.getHeader("Content-Type")));
            dos.writeBytes(String.format("Content-Length: %s\r\n", responseHeaders.getHeader("Content-Length")));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
