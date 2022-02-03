package webserver;

import controller.Controller;
import exception.BadRequestException;
import exception.NotFoundException;
import http.request.HttpRequest;
import http.request.HttpRequestFactory;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            try {
                HttpRequest request = HttpRequestFactory.getHttpRequest(in);
                Controller controller = ControllerType.getControllerType(request.getMethod(),
                        request.getUrl());
                HttpResponse httpResponse = controller.run(request, dos);
                httpResponse.sendResponse();
            } catch (BadRequestException exception) {
                log.error(exception.getMessage());
                getBadRequestResponse(dos).sendResponse();
            } catch (NotFoundException exception) {
                log.error(exception.getMessage());
                getNotFound(dos).sendResponse();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse getBadRequestResponse(DataOutputStream dos) {
        return HttpResponse.badRequest(dos);
    }

    private HttpResponse getNotFound(DataOutputStream dos) {
        return HttpResponse.notFound(dos);
    }
}
