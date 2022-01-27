package webserver.http.request;

import util.Constants;
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
        HttpRequestLine httpRequestLine = createHttpRequestLine(br);
        return new HttpRequest(httpRequestLine, new HttpRequestHeader(new HashMap<>()));
    }

    private HttpRequestLine createHttpRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        Map<String, String> requestLine = HttpRequestUtils.parseRequest(line);
        return new HttpRequestLine(
                requestLine.get(Constants.HTTP_METHOD),
                requestLine.get(Constants.HTTP_URL),
                requestLine.get(Constants.HTTP_VERSION));
    }


}
