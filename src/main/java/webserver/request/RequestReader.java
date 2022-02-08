package webserver.request;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public final class RequestReader {

    private RequestReader() {
    }

    public static Request read(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));
            String requestLine = readLine(br);
            HttpMethod method = HttpRequestUtils.parseHttpMethod(requestLine);
            String uri = HttpRequestUtils.parseUri(requestLine);
            Map<String, String> headers = readHeaderLines(br);
            Map<String, String> body = readBody(br,
                Integer.parseInt(headers.getOrDefault("Content-Length", "0")));

            return Request.builder()
                .method(method)
                .uri(uri)
                .headers(Collections.unmodifiableMap(headers))
                .body(Collections.unmodifiableMap(body))
                .build();
        } catch (Exception e) {
            throw e;
        }
    }

    private static String readLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    private static Map<String, String> readHeaderLines(BufferedReader br) throws Exception {
        Map<String, String> headers = Maps.newHashMap();
        String headerLine = readLine(br);
        while (!headerLine.equals("")) {
            Pair header = HttpRequestUtils.parseHeader(headerLine);
            headers.put(header.getKey(), header.getValue());
            headerLine = readLine(br);
        }
        return headers;
    }

    private static Map<String, String> readBody(BufferedReader br, int len) throws IOException {
        if (len > 0) {
            char[] buf = new char[len];
            br.read(buf);
            return HttpRequestUtils.parseQueryString(String.valueOf(buf));
        }
        return Maps.newHashMap();
    }
}
