package webserver.requesthandler;

import application.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
            byte[] responseBody = handleRequest(httpRequest);

            HttpResponse.doResponse(out, httpRequest.getHeaders(), responseBody);
        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] handleRequest(HttpRequest httpRequest) throws IOException {
        HttpRequestStartLine startLine = httpRequest.getStartLine();
        Map<String, String> requestBody = httpRequest.getRequestBody();
        String url = startLine.getUrl();

        String redirectTo = "./webapp" + url;
        if (Objects.equals(url, "/users")) {
            UserService.create(requestBody);
            redirectTo = "./webapp/index.html";
        }

        log.debug("[Redirect] " + redirectTo);
        return Files.readAllBytes(new File(redirectTo).toPath());
    }
}
