package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.RequestMethod.getRequestMethod;

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

            // Implicit GET
            String[] filePath = httpRequest.getUri().split("\\.");
            // Extract extension
            String textType = filePath[filePath.length - 1];
            Path path = Paths.get("./webapp" + httpRequest.getUri());

            byte[] body = Files.readAllBytes(path);
            response200Header(dos, body.length, textType);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
        httpRequestBuilder.requestMethod(getRequestMethod(splitRequestLine[0]));
        httpRequestBuilder.uri(splitRequestLine[1]);
        httpRequestBuilder.httpVersion(splitRequestLine[2]);
    }

    private void parseRequestHeaders(BufferedReader br,
                                     HttpRequest.HttpRequestBuilder httpRequestBuilder) throws IOException {
        // No support for any headers yet
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String text_type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" + text_type + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
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
