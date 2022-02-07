package webserver;

import network.HttpRequest;
import network.HttpResponse;
import network.Method;
import network.Status;
import webserver.service.UserService;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    private UserService userService;
    private HttpRequest httpRequest;
    private OutputStream outputStream;
    private Status status;

    public ResponseHandler(HttpRequest httpRequest, OutputStream outputStream){
        this.httpRequest = httpRequest;
        this.outputStream = outputStream;
        this.userService = new UserService(httpRequest);
    }

    public void run() throws IOException {
        String path = httpRequest.getPath();
        Method method = httpRequest.getMethod();

        switch (method){
            case GET:
                getController(path);
                break;
            case POST:
                postController(path);
                break;
        }

        HttpResponse.handleHtmlResponse(path, outputStream, status);
    }

    private void getController(String path){
        status = Status.OK;
    }

    private void postController(String path){

        switch (path){
            case "/user/create":
                status = userService.signUp();
                break;
        }

    }
}
