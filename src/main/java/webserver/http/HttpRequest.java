package webserver.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final int INDEX_OF_HTTP_METHOD = 0;
    private static final int INDEX_OF_URL = 1;
    private static final int INDEX_OF_URL_PATH = 0;
    private static final int INDEX_OF_URL_QUERY_STRING = 1;
    private static final String queryStringRegex = "\\?";

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> queryStrings;
    private final HttpHeaders headers;
    private final HttpRequestParams requestParams;
    private final Cookies cookies;

    public HttpRequest(BufferedReader br) throws IOException {
        String[] requestLine = splitRequestLine(br.readLine());
        this.httpMethod = HttpMethod.parseHttpMethod(requestLine[INDEX_OF_HTTP_METHOD]);

        String[] requestUrl = splitRequestUrl(requestLine[INDEX_OF_URL]);
        this.url = requestUrl[INDEX_OF_URL_PATH];
        this.queryStrings = requestUrl.length > 1 ? parseQueryStrings(URLDecoder.decode(requestUrl[INDEX_OF_URL_QUERY_STRING], StandardCharsets.UTF_8)) : new HashMap<>();

        this.headers = parseHttpHeader(br);
        this.requestParams = parseHttpRequestParams(br);
        this.cookies = parseCookies();
        log.info("request params : {}", requestParams);
    }

    private String[] splitRequestLine(String requestLine) {
        if (StringUtils.isEmpty(requestLine)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        return requestLine.split(StringUtils.SPACE);
    }

    private String[] splitRequestUrl(String requestUrl) {
        return requestUrl.split(queryStringRegex);
    }

    private HttpHeaders parseHttpHeader(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line = br.readLine();
        while (!StringUtils.isEmpty(line)) {
            log.info(line);
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.addHeaders(pair.getKey(), pair.getValue());
            line = br.readLine();
        }
        return headers;
    }

    private Map<String, String> parseQueryStrings(String queryStrings) {
        return HttpRequestUtils.parseQueryString(queryStrings);
    }

    private HttpRequestParams parseHttpRequestParams(BufferedReader br) throws IOException {
        String requestParams = IOUtils.readData(br, headers.getContentLength());
        return new HttpRequestParams(HttpRequestUtils.parseQueryString(URLDecoder.decode(requestParams, StandardCharsets.UTF_8)));
    }

    public Cookies parseCookies() {
        Cookies cookies = new Cookies();
        HttpRequestUtils.parseCookies(headers.get("Cookie").orElse(""))
                .forEach(cookies::addCookie);
        return cookies;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryStrings() {
        return queryStrings;
    }

    public HttpRequestParams getRequestParams() {
        return requestParams;
    }

    public Cookie getCookieByName(String name) {
        return cookies.getCookieByName(name).orElse(null);
    }
}
