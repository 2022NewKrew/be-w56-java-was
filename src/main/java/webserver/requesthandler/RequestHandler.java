package webserver.requesthandler;

import application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import webserver.requesthandler.httprequest.HttpRequest;
import webserver.requesthandler.httprequest.HttpRequestStartLine;
import webserver.requesthandler.httpresponse.HttpResponse;
import webserver.requesthandler.httpresponse.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RequestHandler extends Thread {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = HttpRequest.doRequest(br);
            HttpResponse httpResponse = handleRequest(httpRequest);

            httpResponse.doResponse(out);
        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        HttpRequestStartLine startLine = httpRequest.getStartLine();
        Map<String, String> requestBody = httpRequest.getBody();
        Map<String, String> requestHeader = httpRequest.getHeader();
        String contentType = requestHeader.get("Accept").split(",")[0];
        Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", contentType);

        String url = startLine.getUrl();
        HttpStatus httpStatus = HttpStatus.valueOf(200);
        String redirectTo = "./webapp" + url;

        if (Objects.equals(url, "/users")) {
            log.debug("Request to: /users");
            UserService.create(requestBody);
            httpStatus = HttpStatus.valueOf(302);
            redirectTo = "./webapp/index.html";
        }

        log.debug("[Redirect] " + redirectTo);
        byte[] responseBody = Files.readAllBytes(new File(redirectTo).toPath());
        return HttpResponse.valueOf(httpStatus, responseHeader, startLine.getVersion(), responseBody);
    }
}
