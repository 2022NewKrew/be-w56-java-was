package http.request;

import com.google.common.base.Strings;
import http.header.HttpHeaderNames;
import http.header.HttpHeaders;
import http.util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * {@link BufferedReader}에서 {@link HttpRequest} 디코딩해 반환하는 클래스
 */
public class HttpRequestDecoder {

    public static HttpRequest decode(BufferedReader br) throws IOException {
        String line;
        String[] tokens;

        line = br.readLine();
        tokens = line.split(" ");

        if (tokens.length != 3) {
            throw new IOException();
        }

        HttpRequestMethod method = HttpRequestMethod.valueOf(tokens[0]);
        String uri = tokens[1];
        String protocolVersion = tokens[2];

        HttpHeaders headers = getHeaders(br);

        // read body if "content-length" header exists
        String body = null;
        if (headers.containsName(HttpHeaderNames.CONTENT_LENGTH.toString())) {
            int contentLength = Integer.parseInt(headers.getValue(HttpHeaderNames.CONTENT_LENGTH.toString()));
            body = HttpRequestUtils.readData(br, contentLength);
        }

        return HttpRequest.builder()
                .method(method)
                .uri(uri)
                .protocolVersion(protocolVersion)
                .headers(headers)
                .body(body)
                .build();
    }

    /* ---------------------------------------------------------------------- */

    private static HttpHeaders getHeaders(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        String line;
        String[] tokens;
        while (true) {
            line = br.readLine();
            if (Strings.isNullOrEmpty(line)) {
                break;
            }

            tokens = line.split(": ");
            if (tokens.length != 2) {
                throw new IOException();
            }

            headers.add(tokens[0], tokens[1]);
        }

        return headers;
    }
}
