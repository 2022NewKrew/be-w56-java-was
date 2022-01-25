package webserver.model;

import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private static final int INDEX_OF_HTTP_METHOD = 0;
    private static final int INDEX_OF_URL = 1;

    private final HttpMethod httpMethod;
    private final String url;
    private final HttpHeaders headers;

    public HttpRequest(BufferedReader br) throws IOException {
        String[] requestLine = br.readLine().split(" ");
        this.httpMethod = HttpMethod.parseHttpMethod(requestLine[INDEX_OF_HTTP_METHOD]);
        this.url = requestLine[INDEX_OF_URL];
        this.headers = new HttpHeaders();
        String line = br.readLine();
        while (!StringUtils.isEmpty(line)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.addHeaders(pair.getKey(), pair.getKey());
            line = br.readLine();
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }
}
