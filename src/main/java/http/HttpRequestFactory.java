package http;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class HttpRequestFactory {

    private HttpRequestFactory() {
    }

    public static HttpRequest newInstance(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String[] tokens = IOUtils.readRequestLine(br);
        Url url = new Url(HttpMethod.of(tokens[0]), tokens[1]);
        HttpVersion version = HttpVersion.of(tokens[2]);
        Map<HttpHeader, String> headers = parseHeaders(br);
        Map<String, String> parameters = parseBody(br, headers);

        return HttpRequest.builder()
            .url(url)
            .version(version)
            .headers(headers)
            .parameters(parameters)
            .build();
    }

    private static Map<HttpHeader, String> parseHeaders(BufferedReader br) throws IOException {
        List<String> lines = IOUtils.readHeaders(br);
        return lines.stream()
            .map(HttpRequestUtils::parseHeader)
            .filter(pair -> HttpHeader.contains(pair.getKey()))
            .collect(Collectors.toMap(pair -> HttpHeader.of(pair.getKey()), Pair::getValue));
    }

    private static Map<String, String> parseBody(BufferedReader br,
        Map<HttpHeader, String> headers) throws IOException {
        Map<String, String> parameters = Maps.newHashMap();
        if (headers.containsKey(HttpHeader.CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.get(HttpHeader.CONTENT_LENGTH));
            String body = IOUtils.readBody(br, contentLength);
            parameters = HttpRequestUtils.parseQueryString(body);
        }
        return parameters;
    }
}
