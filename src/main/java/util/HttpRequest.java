package util;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequest {

    private String method;
    private String path;
    private String httpVersion;
    private int contentLength = 0;

    private Map<String, String> pathParameter = new HashMap<>();
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest(String rawReqeust) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rawReqeust.getBytes(StandardCharsets.UTF_8))));
            parseFirstLine(br);
            parseHeader(br);
            parseBody(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseFirstLine(BufferedReader br) throws IOException {
        List<String> firstLines = Arrays.asList(br.readLine().split(" "));
        this.method = firstLines.get(0);
        this.path = firstLines.get(1);
        this.httpVersion = firstLines.get(2);
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String line;
        while (true) {
            line = br.readLine();
            if (line.equals("")) { break; }
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
        }
        if (headers.containsKey("Content-Length")) { this.contentLength = Integer.parseInt(headers.get("Content-Length")); }
    }

    private void parseBody(BufferedReader br) throws IOException {
        this.body = IOUtils.readData(br, contentLength);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public int getContentLength() {
        return contentLength;
    }

    public Map<String, String> getPathParameter() {
        return pathParameter;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
