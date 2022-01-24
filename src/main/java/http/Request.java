package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final String version;
    private final Headers headers;
    private final String body;

    private Request(
            String method,
            String path,
            String version,
            Headers headers,
            String body
    ) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
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
        String path = split[1];
        String version = split[2];
        Headers headers = parseHeaders(br);
        String body = parseBody(headers, br);
        return Request.newBuilder()
                .method(method)
                .path(path)
                .version(version)
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
            return "";
        }
        int length = Integer.parseInt(contentLength);
        char[] chars = new char[length];
        br.read(chars);
        return String.valueOf(chars);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private static class Builder {

        private String method;
        private String path;
        private String version;
        private Headers headers;
        private String body;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder version(String version) {
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
            return new Request(method, path, version, headers, body);
        }
    }
}
