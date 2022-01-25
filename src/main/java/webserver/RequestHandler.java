package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpResponse;
import util.HttpStatus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.HttpMethod.getHttpMethod;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        // 1. Parse the http request to form servletReq
        // 2. Search url in the url to handler mapper
        // 3. Call the matching url handler(servlet, req, res)
        // 4. Post-process the response(header+body) and send it to client
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest httpRequest = parseRequest(br);
            // TODO: Http version should be provided by the handler. Copying from request is wrong.
            HttpResponse httpResponse = new HttpResponse(httpRequest, httpRequest.getHttpVersion());
            hardcodedHandle(httpRequest, httpResponse);
            dos.write(httpResponse.toByte());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void hardcodedHandle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // Implicit GET
        String[] filePath = httpRequest.getUri().split("\\.");
        // Extract extension
        String textType = filePath[filePath.length - 1];
        Path path = Paths.get("./webapp" + httpRequest.getUri());

        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setContentType("text/" + textType + ";charset=utf-8");
        httpResponse.setBody(Files.readAllBytes(path));
    }

    private HttpRequest parseRequest(BufferedReader br) throws IOException {
        HttpRequest.HttpRequestBuilder httpRequestBuilder = HttpRequest.builder();
        parseRequestLine(br, httpRequestBuilder);
        parseRequestHeaders(br, httpRequestBuilder);
        return httpRequestBuilder.build();
    }

    private void parseRequestLine(BufferedReader br,
                                  HttpRequest.HttpRequestBuilder httpRequestBuilder) throws IOException {
        String requestLine = br.readLine();
        log.info(requestLine);

        String[] splitRequestLine = requestLine.split(" ");
        httpRequestBuilder.httpMethod(getHttpMethod(splitRequestLine[0]));
        httpRequestBuilder.uri(splitRequestLine[1]);
        httpRequestBuilder.httpVersion(splitRequestLine[2]);
    }

    private void parseRequestHeaders(BufferedReader br,
                                     HttpRequest.HttpRequestBuilder httpRequestBuilder) throws IOException {
        // No support for any headers yet
    }
}
