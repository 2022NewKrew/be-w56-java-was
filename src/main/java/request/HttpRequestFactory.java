package request;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestFactory {

    private static HttpRequestFactory INSTANCE;

    private HttpRequestFactory() {}

    public static HttpRequestFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestFactory();
        }
        return INSTANCE;
    }

    public HttpRequest createHttpRequest(BufferedReader br) throws IOException {
        HttpRequestLine httpRequestLine = createHttpRequestLine(br.readLine());
        return new HttpRequest(httpRequestLine, new HttpRequestHeader(new HashMap<>()));
    }

    private HttpRequestLine createHttpRequestLine(String line) throws IOException {
        Map<String, String> requestLine = HttpRequestUtils.parseRequest(line);
        return new HttpRequestLine(requestLine.get("method"), requestLine.get("url"), requestLine.get("httpVersion"));
    }


}
