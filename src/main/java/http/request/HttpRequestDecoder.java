package http.request;

import http.cookie.Cookie;
import http.header.HttpHeaderName;
import http.header.HttpHeaders;
import http.header.HttpProtocolVersion;
import http.util.HttpRequestUtils;
import http.view.InputView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link BufferedReader}에서 {@link HttpRequest} 디코딩해 반환하는 클래스
 */
public class HttpRequestDecoder {

    private static final String START_LINE_DELIMITER = " ";
    private static final String HEADER_DELIMITER = ": ";

    public static HttpRequest decode(BufferedReader br) throws IOException {
        String[] tokens = InputView.readTokenizedLine(br, START_LINE_DELIMITER);

        if (tokens.length != 3) {
            throw new IOException();
        }

        HttpRequestMethod method = HttpRequestMethod.valueOf(tokens[0]);
        String uri = tokens[1];
        HttpProtocolVersion protocolVersion = HttpProtocolVersion.parseProtocolVersion(tokens[2]);

        HttpHeaders headers = getHeaders(br);
        List<Cookie> cookies = new ArrayList<>();

        if (headers.containsName("cookie")) {
            cookies = HttpRequestUtils.parseCookies(headers.getValue("cookie"));
            headers.remove("cookie");
        }

        // read body if "content-length" header exists
        String body = null;
        if (headers.containsName(HttpHeaderName.CONTENT_LENGTH.getValue())) {
            int contentLength = Integer.parseInt(headers.getValue(HttpHeaderName.CONTENT_LENGTH.getValue()));
            body = HttpRequestUtils.readData(br, contentLength);
            body = URLDecoder.decode(body, StandardCharsets.UTF_8);
        }

        return HttpRequest.builder()
                .method(method)
                .uri(uri)
                .protocolVersion(protocolVersion)
                .cookies(cookies)
                .headers(headers)
                .body(body)
                .build();
    }

    /* ---------------------------------------------------------------------- */

    private static HttpHeaders getHeaders(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        while (true) {
            String[] tokens = InputView.readTokenizedLine(br, HEADER_DELIMITER);
            if (tokens.length == 0) {
                break;
            } else if (tokens.length != 2) {
                throw new IOException("Header format error");
            }

            headers.add(tokens[0], tokens[1]);
        }

        return headers;
    }
}
