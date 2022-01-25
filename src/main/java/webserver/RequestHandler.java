package webserver;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import application.service.UserService;
import webserver.common.util.HttpRequestStartLine;
import webserver.common.util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RequestHandler extends Thread {

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            HttpRequestStartLine startLine = inputStartLine(br);
            Map<String, String> headers = inputHeaders(br);
            byte[] body = handleRequest(startLine);

            outputResponse(out, headers, body);
        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void outputResponse(OutputStream out, Map<String, String> headers, byte[] body) {
        log.debug("[HTTP Response]");
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, headers.get("Accept").split(",")[0]);
        responseBody(dos, body);
    }

    private Map<String, String> inputHeaders(BufferedReader br) throws IOException {
        Map<String, String> parsedHeaders = new HashMap<>();
        String line = null;
        log.debug("[Headers]");
        while (!"".equals(line)) {
            line = br.readLine();
            log.debug(line);
            if (Strings.isNullOrEmpty(line)) {
                break;
            }
            HttpRequestUtils.Pair parsed = HttpRequestUtils.parseHeader(line);
            parsedHeaders.put(parsed.getKey(), parsed.getValue());
        }
        return parsedHeaders;
    }

    private HttpRequestStartLine inputStartLine(BufferedReader br) throws IOException {
        HttpRequestStartLine startLine = HttpRequestUtils.parseStartLine(br.readLine());
        assert startLine != null;
        log.debug("[HTTP Request]");
        log.debug("[Start line] " + startLine);
        return startLine;
    }

    private byte[] handleRequest(HttpRequestStartLine startLine) throws IOException {
        String url = startLine.getUrl();
        Map<String, String> queryParameters = startLine.getQueryParameters();

        String redirectTo = url;
        if (Objects.equals(url, "/user/create")) {
            UserService.create(queryParameters);
            redirectTo = "/index.html";
        }

        log.debug("[Redirect] " + redirectTo);
        return Files.readAllBytes(new File("./webapp" + redirectTo).toPath());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
