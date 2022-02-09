package lib.was.http;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class Response {

    private final int status;
    private final Headers headers;
    private final byte[] body;

    private Response(int status, Headers headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public Response(int status, Headers headers, String body) {
        this(status, headers, body.getBytes(StandardCharsets.UTF_8));
    }

    public static Response ok(String body) {
        return new Response(200, Headers.EMPTY, body);
    }

    public static Response ok(byte[] body) {
        return new Response(200, Headers.EMPTY, body);
    }

    public static Response found(String body) {
        return new Response(302, Headers.EMPTY, body);
    }

    public static Response notFound(String body) {
        return new Response(404, Headers.EMPTY, body);
    }

    public static Response error(String body) {
        return new Response(500, Headers.EMPTY, body);
    }

    public Response contentType(ContentType contentType) {
        return header("Content-Type", contentType.getContentType());
    }

    public Response redirect(String location) {
        return header("Location", location);
    }

    public Response setCookie(String name, String value) {
        String cookies = Optional.ofNullable(headers.get("Set-Cookie"))
                    .map(s -> s + "; ")
                    .orElse("");
        return header("Set-Cookie", cookies + name + "=" + value);
    }

    public Response header(String key, String value) {
        return new Response(status, headers.plus(key, value), body);
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
