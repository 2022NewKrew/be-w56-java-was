package webserver;

import util.HttpRequestUtils;
import webserver.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private final HttpMethod method;
    private final String uri;
    private final String version;

    public HttpRequest(HttpMethod method, String uri, String version) {
        this.method = method;
        this.uri = uri;
        this.version = version;
    }

    public static HttpRequest of(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String requestLine = br.readLine();
        String[] tokens = HttpRequestUtils.parseRequestLine(requestLine);

        return new HttpRequest(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2]);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }
}
