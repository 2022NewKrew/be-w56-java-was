package webserver.model;

import org.apache.commons.lang3.StringUtils;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final int INDEX_OF_HTTP_METHOD = 0;
    private static final int INDEX_OF_URL = 1;
    private static final int INDEX_OF_URL_PATH = 0;
    private static final int INDEX_OF_URL_QUERY_STRING = 1;
    private static final String queryStringRegex = "\\?";

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> queryStrings;
    private final HttpHeaders headers;

    public HttpRequest(BufferedReader br) throws IOException {
        String[] requestLine = splitRequestLine(br.readLine());
        this.httpMethod = HttpMethod.parseHttpMethod(requestLine[INDEX_OF_HTTP_METHOD]);

        String[] requestUrl = splitRequestUrl(requestLine[INDEX_OF_URL]);
        this.url = requestUrl[INDEX_OF_URL_PATH];
        this.queryStrings = requestUrl.length > 1 ? parseQueryStrings(requestUrl[INDEX_OF_URL_QUERY_STRING]) : new HashMap<>();

        this.headers = parseHttpHeader(br);
    }

    private String[] splitRequestLine(String requestLine) {
        return requestLine.split(StringUtils.SPACE);
    }

    private String[] splitRequestUrl(String requestUrl) {
        return requestUrl.split(queryStringRegex);
    }

    private HttpHeaders parseHttpHeader(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line = br.readLine();
        while (!StringUtils.isEmpty(line)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.addHeaders(pair.getKey(), pair.getKey());
            line = br.readLine();
        }
        return headers;
    }

    private Map<String, String> parseQueryStrings(String queryStrings) {
        return HttpRequestUtils.parseQueryString(queryStrings);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }
}
