package http;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

    private Url url;
    private HttpVersion version;
    private final Map<HttpHeader, String> headers;
    private Map<String, String> fields;

    public HttpRequest() {
        version = HttpVersion.HTTP_1_1;
        headers = Maps.newHashMap();
        fields = Maps.newHashMap();
    }

    public void parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        parseRequestLine(IOUtils.readRequestLine(br));
        parseHeaders(IOUtils.readHeaders(br));
        String contentLength = headers.get(HttpHeader.CONTENT_LENGTH);
        if (contentLength != null) {
            parseBody(
                IOUtils.readBody(br, Integer.parseInt(headers.get(HttpHeader.CONTENT_LENGTH))));
        }
    }

    private void parseRequestLine(String[] tokens) {
        url = new Url(HttpMethod.of(tokens[0]), tokens[1]);
        version = HttpVersion.of(tokens[2]);
    }

    private void parseHeaders(List<String> lines) {
        lines.stream()
            .map(HttpRequestUtils::parseHeader)
            .filter(pair -> HttpHeader.of(pair.getKey()) != null)
            .forEach(pair -> putHeader(HttpHeader.of(pair.getKey()), pair.getValue()));
    }

    private void parseBody(String body) {
        fields = HttpRequestUtils.parseQueryString(body);
    }

    public void putHeader(HttpHeader header, String value) {
        headers.put(header, value);
    }

    public HttpResponse respond() {
        return new HttpResponse(version, HttpStatus.OK, headers, null, null);
    }

    public boolean hasAllFields(List<String> fields) {
        if (this.fields.isEmpty()) {
            return false;
        }
        return fields.stream().allMatch(field -> this.fields.containsKey(field));
    }

    public Url getUrl() {
        return url;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public Map<HttpHeader, String> getHeaders() {
        return headers;
    }
}
