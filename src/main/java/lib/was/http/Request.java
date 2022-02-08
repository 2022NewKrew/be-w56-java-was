package lib.was.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Request {

    private final Method method;
    private final Locator locator;
    private final Version version;
    private final Headers headers;
    private final String body;

    private Request(
            Method method,
            Locator locator,
            Version version,
            Headers headers,
            String body
    ) {
        this.method = method;
        this.locator = locator;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return locator.getPath();
    }

    public Map<String, String> getQueryParams() {
        return locator.getQuery();
    }

    public Version getVersion() {
        return version;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getBody() {
        return body;
    }

    public static Request parse(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        String[] split = line.split(" ");
        String method = split[0];
        String locator = split[1];
        String version = split[2];
        Headers headers = parseHeaders(br);
        String body = parseBody(headers, br);
        return Request.newBuilder()
                .method(Method.fromString(method).orElseThrow(() -> new IllegalArgumentException("Invalid method")))
                .locator(Locator.parse(locator))
                .version(Version.fromString(version).orElseThrow(() -> new IllegalArgumentException("Invalid version")))
                .headers(headers)
                .body(body)
                .build();
    }

    private static Headers parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> out = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] split = line.split(":");
            String key = split[0];
            String[] subarray = Arrays.copyOfRange(split, 1, split.length);
            String value = String.join(":", subarray)
                    .replaceFirst("^\\s+", "");
            out.put(key, value);
        }
        return new Headers(out);
    }

    private static String parseBody(Headers headers, BufferedReader br) throws IOException {
        String contentLength = headers.get("Content-Length");
        if (contentLength == null) {
            return null;
        }
        int length = Integer.parseInt(contentLength);
        char[] chars = new char[length];
        br.read(chars);
        return URLDecoder.decode(new String(chars), StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return method == request.method &&
                Objects.equals(locator, request.locator) &&
                version == request.version &&
                Objects.equals(headers, request.headers) &&
                Objects.equals(body, request.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, locator, version, headers, body);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Method method;
        private Locator locator;
        private Version version;
        private Headers headers;
        private String body;

        private Builder() {}

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder locator(Locator locator) {
            this.locator = locator;
            return this;
        }

        public Builder version(Version version) {
            this.version = version;
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Request build() {
            return new Request(method, locator, version, headers, body);
        }
    }
}
