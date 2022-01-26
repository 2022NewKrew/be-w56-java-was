package webserver;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import application.service.UserService;
import webserver.common.util.HttpRequestStartLine;
import webserver.common.util.HttpRequestUtils;
import webserver.common.util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
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

            HttpRequestStartLine startLine = inputStartLine(br.readLine());
            Map<String, String> headers = inputHeaders(br);
            Map<String, String> requestBody = Collections.emptyMap();
            if (headers.containsKey("Content-Length")) {
                requestBody = inputBody(IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))));
            }
            byte[] body = handleRequest(startLine, requestBody);

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

    private HttpRequestStartLine inputStartLine(String startLineString) throws IOException {
        log.debug("[HTTP Request]");
        log.debug("[Start line] " + startLineString);
        startLineString = URLDecoder.decode(startLineString, StandardCharsets.UTF_8);

        HttpRequestStartLine startLine = HttpRequestUtils.parseStartLine(startLineString);
        assert startLine != null;
        return startLine;
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

    private Map<String, String> inputBody(String requestBodyString) {
        log.debug("[Request body] " + requestBodyString);
        requestBodyString = URLDecoder.decode(requestBodyString, StandardCharsets.UTF_8);
        return HttpRequestUtils.parseQueryString(requestBodyString);
    }

    private byte[] handleRequest(HttpRequestStartLine startLine, Map<String, String> requestBody) throws IOException {
        String url = startLine.getUrl();

        String redirectTo = "./webapp" + url;
        if (Objects.equals(url, "/users")) {
            UserService.create(requestBody);
            redirectTo = "./webapp/index.html";
        }

        log.debug("[Redirect] " + redirectTo);
        return Files.readAllBytes(new File(redirectTo).toPath());
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
