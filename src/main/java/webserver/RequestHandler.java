package webserver;

import controller.Controller;
import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import httpmodel.HttpStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestConverter;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final RequestMapping REQUEST_MAPPING = new RequestMapping();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(),
            connection.getPort());
        logger.debug("쓰레드 이름 : {}", Thread.currentThread().getName());

        try (final InputStream inputStream = connection.getInputStream();
            final OutputStream outputStream = connection.getOutputStream();
            final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        ) {
            HttpResponse response = messageConvert(bufferedReader);
            outputStream.write(response.message().getBytes());
            outputStream.write(response.getBody());
            outputStream.flush();
        } catch (IOException exception) {
            logger.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private HttpResponse messageConvert(BufferedReader bufferedReader) throws IOException {
        HttpResponse response = new HttpResponse();
        try {
            final HttpRequest httpRequest = HttpRequestConverter.createdRequest(bufferedReader);
            logger.info("요청한 url : {}", httpRequest.getUri());
            Controller controller = REQUEST_MAPPING.getController(httpRequest);
            controller.service(httpRequest, response);
            return response;
        } catch (Exception exception) {
            logger.error("알수없는 에러가 발생", exception);
            response.setErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            logger.error("Exception closing socket", exception);
        }
    }
}
