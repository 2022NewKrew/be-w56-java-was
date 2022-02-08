package webserver;

import network.HttpRequest;
import network.Method;
import webserver.controller.*;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    private PostController postController;
    private GetController getController;
    private HttpRequest httpRequest;

    public ResponseHandler(HttpRequest httpRequest, OutputStream outputStream){
        this.httpRequest = httpRequest;
        this.postController = new PostController(httpRequest, outputStream);
        this.getController = new GetController(httpRequest, outputStream);
    }

    public void run() throws IOException {
        String path = httpRequest.getPath();
        Method method = httpRequest.getMethod();

        switch (method){
            case GET:
                getController.handleGet(path);
                break;
            case POST:
                postController.handlePost(path);
                break;
        }

    }


}
