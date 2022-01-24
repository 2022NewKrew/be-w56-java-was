package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private static final int INDEX_OF_HTTP_METHOD = 0;
    private static final int INDEX_OF_URL = 1;

    private final HttpMethod httpMethod;
    private final String url;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] requestLine = br.readLine().split(" ");
        this.httpMethod = HttpMethod.parseHttpMethod(requestLine[INDEX_OF_HTTP_METHOD]);
        this.url = requestLine[INDEX_OF_URL];
    }

    public String getUrl() {
        return url;
    }
}
