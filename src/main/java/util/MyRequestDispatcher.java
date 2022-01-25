package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class MyRequestDispatcher {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private String viewPath;

    public MyRequestDispatcher(String viewPath) {
        this.viewPath = viewPath;
    }

    public void forward(MyHttpRequest request, MyHttpResponse response) throws IOException {
            response.setBody(viewPath);

            if(response.getStatus() == MyHttpResponseStatus.OK) {
                response200Header(response.getDos(), request.getAccept(), response.getBody().length);
                responseBody(response.getDos(), response.getBody());
            }

    }

    private void response200Header(DataOutputStream dos, String accept, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + accept + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
