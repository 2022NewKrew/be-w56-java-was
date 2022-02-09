package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private String protocol;
    private String MIME;
    private Map<String, String> headers;
    private String body;
    private Map<String, String> query;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public HttpRequest(InputStream in) throws IOException {
        parse(in);
    }

    private void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // requestLine parsing
        String line = br.readLine();
        System.out.println(line);
        Map<String, String> requestLine = HttpRequestUtils.parseRequestLine(line);
        method = requestLine.get("method");
        url = requestLine.get("url");
        protocol = requestLine.get("protocol");
        query = HttpRequestUtils.parseQueryString(requestLine.get("query"));

        // header parsing
        headers = HttpRequestUtils.parseResponseHeader(br);
        MIME = headers.get("Accept").split(",")[0];
        if (method.equals("POST")) {
            body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            log.info("BODYVALUE " + body);
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
