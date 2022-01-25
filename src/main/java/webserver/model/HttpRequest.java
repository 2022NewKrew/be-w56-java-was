package webserver.model;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private static final int INDEX_OF_HTTP_METHOD = 0;
    private static final int INDEX_OF_URL = 1;

    private final HttpMethod httpMethod;
    private final String url;

    public HttpRequest(BufferedReader br) throws IOException {
        String[] requestLine = br.readLine().split(" ");
        this.httpMethod = HttpMethod.parseHttpMethod(requestLine[INDEX_OF_HTTP_METHOD]);
        this.url = requestLine[INDEX_OF_URL];
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }
}
