package lib.was.http;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Response {

    private final int status;
    private final Headers headers;
    private final byte[] body;

    public Response(int status, Headers headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public static Response of(int status, Headers headers, String body) {
        return new Response(status, headers, body.getBytes(StandardCharsets.UTF_8));
    }

    public static Response ok(Headers headers, String body) {
        return of(200, headers, body);
    }

    public static Response notFound(Headers headers, String body) {
        return of(404, headers, body);
    }

    public static Response error(Headers headers, String message) {
        return of(500, headers, message);
    }

    public int getStatusCode() {
        return status;
    }

    public String getStatusMessage() {
        switch (status) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
        }
        return "";
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Iterable<? extends Map.Entry<String, String>> getHeaders() {
        return headers.entrySet();
    }

    public byte[] getBody() {
        return body;
    }
}
