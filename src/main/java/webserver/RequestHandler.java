package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("요청: IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            List<String> lineList = HttpRequestUtils.convertToStringList(bufferedReader);
            HttpRequest httpRequest = HttpRequest.of(lineList);

            String path = httpRequest.getPath();
            String contentType = httpRequest.getHeader("Accept").split(",")[0]; // TODO - Accept 없는 경우 처리

            HttpResponse httpResponse = HttpResponse.of(path, contentType);
            httpResponse.sendResponse(connection);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
