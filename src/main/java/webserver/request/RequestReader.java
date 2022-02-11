package webserver.request;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public final class RequestReader {

    private RequestReader() {
    }

    public static Request read(InputStream in) throws Exception {
        try {
            // br.close() 하면 Socket이 닫히는 exception 발생
            // BufferedReader.close() 에서 타고 들어가 InputStream in도 close해서 문제가 생기는 것 같음 (잘 모르겠음)
            BufferedReader br = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));
            String requestLine = readLine(br);
            HttpMethod method = HttpRequestUtils.parseHttpMethod(requestLine);
            String uri = HttpRequestUtils.parseUri(requestLine);
            MultiValueMap<String, String> headers = readHeaderLines(br);
            Map<String, String> body = readBody(br,
                Integer.parseInt(headers.getOrDefault("Content-Length", List.of("0")).get(0)));

            return Request.builder()
                .method(method)
                .uri(uri)
                .headers(CollectionUtils.unmodifiableMultiValueMap(headers))
                .body(Collections.unmodifiableMap(body))
                .build();
        } catch (Exception e) {
            throw e;
        }
    }

    private static String readLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    private static MultiValueMap<String, String> readHeaderLines(BufferedReader br)
        throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String headerLine = readLine(br);
        while (!headerLine.equals("")) {
            Pair header = HttpRequestUtils.parseHeader(headerLine);
            headers.add(header.getKey(), header.getValue());
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
