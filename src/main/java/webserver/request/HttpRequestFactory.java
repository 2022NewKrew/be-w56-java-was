package webserver.request;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton
 */
public class HttpRequestFactory {

    private static HttpRequestFactory INSTANCE;

    private HttpRequestFactory() {
    }

    public static HttpRequestFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestFactory();
        }
        return INSTANCE;
    }

    public HttpRequest createBy(BufferedReader br) throws IOException {
        HttpRequestLine httpRequestLine = createRequestLine(br);
        HttpRequestHeader httpRequestHeader = createRequestHeader(br);
        HttpRequestBody httpRequestBody = createRequestBody(br);
        return new HttpRequest(httpRequestLine, httpRequestHeader, httpRequestBody);
    }

    private HttpRequestLine createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] elements = line.split(StringUtils.SPACE);
        HttpRequestMethod requestMethod = HttpRequestMethod.valueOf(elements[0]);
        String uri = elements[1];
        String httpVersion = elements[2];
        return new HttpRequestLine(requestMethod, new HttpRequestUri(uri), httpVersion);
    }

    private HttpRequestHeader createRequestHeader(BufferedReader br) throws IOException {
        Map<String, String[]> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()).equals(StringUtils.SPACE)) {
            String[] keyAndValues = StringUtils.split(line, ": ");
            String key = keyAndValues[0];
            String[] values = StringUtils.split(keyAndValues[1], ", ");
            headers.put(key, values);
        }
        return new HttpRequestHeader(headers);
    }

    private HttpRequestBody createRequestBody(BufferedReader br) throws IOException {
        br.readLine();
        String body = br.readLine();
        return new HttpRequestBody(body);
    }

}
