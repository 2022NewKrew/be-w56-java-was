package was.util;

import was.domain.http.Cookie;
import was.domain.http.HttpRequest;
import was.domain.http.HttpResponse;
import was.meta.HttpHeaders;
import was.meta.MediaTypes;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpMapper {
    public HttpRequest toHttpRequest(byte[] requestBytes) {
        final String requestMsg = new String(requestBytes, StandardCharsets.UTF_8);

        final StringTokenizer st = new StringTokenizer(requestMsg, "\n", true);

        final StringTokenizer st2 = new StringTokenizer(getNextToken(st), " ");
        final String method = getNextToken(st2);

        final String pathStr = getNextToken(st2);
        final StringTokenizer st3 = new StringTokenizer(pathStr, "?=&");
        final String path = getNextToken(st3);
        final Map<String, String> queryParams = createQueryParams(st3);

        final String version = getNextToken(st2);

        // /n 넘김
        getNextToken(st);

        final Map<String, String> headers = createHeaders(st);
        final String contentLengthStr = headers.get(HttpHeaders.CONTENT_LENGTH);

        int contentLength = 0;
        if (contentLengthStr != null) {
            contentLength = Integer.parseInt(contentLengthStr);
        }

        final String body = createBody(st, contentLength);

        final Map<String, String> requestParams;
        if (isApplicationXWwwFormUrlEncode(headers)) {
            requestParams = createRequestParams(body);
        } else {
            requestParams = new HashMap<>();
        }

        final Cookie cookie = createCookie(headers.get(HttpHeaders.COOKIE));

        return HttpRequest.builder
                .method(method)
                .path(path)
                .version(version)
                .headers(headers)
                .queryParams(queryParams)
                .requestParams(requestParams)
                .pathVariables(new HashMap<>())
                .requestBody(body)
                .cookie(cookie)
                .build();
    }

    private boolean isApplicationXWwwFormUrlEncode(Map<String, String> headers) {
        final String value = headers.get(HttpHeaders.CONTENT_TYPE);

        if (value == null)
            return false;

        return value.equals(MediaTypes.APPLICATION_X_WWW_FORM_URLENCODED.getValue());
    }

    private String createBody(StringTokenizer st, int contentLength) {
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            sb.append(getNextToken(st));
        }
        return sb.toString();
    }

    private Map<String, String> createRequestParams(String body) {
        final Map<String, String> requestParams = new HashMap<>();
        // 0번째는 path 이후부터 쿼리스트링

        final StringTokenizer st = new StringTokenizer(body, "&");

        while (st.hasMoreTokens()) {
            final StringTokenizer st2 = new StringTokenizer(getNextToken(st), "=");
            if (st2.countTokens() < 2) {
                break;
            }

            final String key = getNextToken(st2);
            final String value = getNextToken(st2);

            requestParams.put(key, value);
        }

        return requestParams;
    }

    private Map<String, String> createQueryParams(StringTokenizer st) {
        final Map<String, String> queryParams = new HashMap<>();
        // 0번째는 path 이후부터 쿼리스트링

        while (st.hasMoreTokens()) {
            final String key = getNextToken(st);
            final String value = getNextToken(st);

            queryParams.put(key, value);
        }

        return queryParams;
    }

    private Cookie createCookie(String cookieStr) {
        if (cookieStr == null)
            return new Cookie();

        final StringTokenizer st = new StringTokenizer(cookieStr, ";");

        final Cookie cookie = new Cookie();
        while (st.hasMoreTokens()) {
            final String token = getNextToken(st);
            final StringTokenizer st2 = new StringTokenizer(token, "=");

            if (st2.countTokens() < 2)
                break;

            final String key = st2.nextToken();
            final String value = st2.nextToken();

            cookie.put(key, value);
        }

        return cookie;
    }

    private Map<String, String> createHeaders(StringTokenizer st) {
        final Map<String, String> headers = new HashMap<>();
        boolean isPreBlank = false;

        while (st.hasMoreTokens()) {
            final String headerLine = getNextToken(st);

            if (headerLine.isBlank()) {
                if (isPreBlank) {
                    break;
                }

                isPreBlank = true;
                continue;
            }

            final StringTokenizer st3 = new StringTokenizer(headerLine, ":");

            if (st3.countTokens() < 2) {
                break;
            }

            final String key = getNextToken(st3);

            final StringBuilder sb = new StringBuilder();
            while (st3.hasMoreTokens()) {
                sb.append(getNextToken(st3));
            }

            headers.put(key, sb.toString());

            isPreBlank = false;
        }
        return headers;
    }

    private String getNextToken(StringTokenizer st) {
        if (!st.hasMoreTokens()) {
            throw new RuntimeException();
        }
        return st.nextToken().trim();
    }

    public HttpResponse toHttpResponse(HttpRequest httpRequest) {
        return HttpResponse.of(httpRequest);
    }
}
