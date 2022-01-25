package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import domain.ContentType;
import domain.HttpRequest;
import domain.HttpStatus;
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
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
             DataOutputStream dos = new DataOutputStream(out)) {

            String requestLine = bufferedReader.readLine();
            log.info("HTTP Request Line : {}", requestLine);

            HttpRequest httpRequest = new HttpRequest(requestLine, HttpRequestUtils.parseHeaders(bufferedReader));
            String requestPath = httpRequest.getRequestPath();
            byte[] body = Files.readAllBytes(new File("./webapp" + requestPath).toPath());

            response(dos, body, HttpRequestUtils.parseContentType(requestPath));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void response(DataOutputStream dos, byte[] body, ContentType contentType) {
        ResponseHandler responseHandler = new ResponseHandler(dos);
        responseHandler.responseHeader(HttpStatus.OK, body.length, contentType.getContentType());
        responseHandler.responseBody(body);
    }
}
