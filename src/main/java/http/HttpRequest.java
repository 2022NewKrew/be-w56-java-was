package http;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

public class HttpRequest {
    private final HttpRequestLine requestLine;
    private final HttpHeaders headers;
    private final String body;

    protected HttpRequest(HttpRequestLine requestLine, HttpHeaders headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine requestLine = getRequestLine(reader);
        HttpHeaders headers = getHeaders(reader);
        String body = getBody(reader, headers);
        return new HttpRequest(requestLine, headers, body);
    }

    private static HttpRequestLine getRequestLine(BufferedReader reader) throws IOException {
        return HttpRequestLine.from(reader.readLine());
    }

    private static HttpHeaders getHeaders(BufferedReader reader) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("")) {
                break;
            }
            headers.add(line);
        }
        return headers;
    }

    private static String getBody(BufferedReader reader, HttpHeaders headers) throws IOException {
        String contentLength = headers.get("Content-Length");
        if (contentLength == null) {
            return null;
        }
        return IOUtils.readData(reader, Integer.parseInt(contentLength));
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public URI getUri() {
        return requestLine.getUri();
    }

    public HttpVersion getVersion() {
        return requestLine.getVersion();
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getCookies() {
        return HttpRequestUtils.parseCookies(headers.get("Cookie"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpRequest)) {
            return false;
        }
        HttpRequest other = (HttpRequest) obj;
        return requestLine.equals(other.requestLine)
                && headers.equals(other.headers)
                && body.equals(other.body);
    }
}
