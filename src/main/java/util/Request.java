package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Request {
    private String method;
    private String url;
    private String protocol;
    private String MIME;
    private Map<String, String> headers;
    private String body;
    private Map<String, String> query;

    public Request(InputStream in) throws IOException {
        parse(in);
    }

    private void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // request parsing
        String line = br.readLine();
        Map<String, String> requestLine = HttpRequestUtils.parseReponseLine(line);
        method = requestLine.get("method");
        url = requestLine.get("url");
        protocol = requestLine.get("protocol");
        query = HttpRequestUtils.parseQueryString(requestLine.get("query"));

        // header parsing
        headers = HttpRequestUtils.parseResponseHeader(br);
        MIME = headers.get("Accept").split(",")[0];

        if (method.equals("POST")) {
            body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMIME() {
        return MIME;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getQuery() {
        return query;
    }
}
