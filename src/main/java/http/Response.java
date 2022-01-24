package http;

import java.util.Collections;
import java.util.Map;

public class Response {

    private final int status;
    private final Map<String, String> headers;
    private final String body;

    private Response(int status, Map<String, String> headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public static Response of(int status, String body) {
        Map<String, String> headers = Collections.singletonMap(
                "Content-Type", "text/html;charset=utf-8"
        );
        return new Response(status, headers, body);
    }

    public static Response ok(String body) {
        return of(200, body);
    }

    public static Response notFound(String body) {
        return of(404, body);
    }

    public static Response error(String message) {
        return of(500, message);
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

    public Iterable<? extends Map.Entry<String, String>> getHeaders() {
        return headers.entrySet();
    }

    public String getBody() {
        return body;
    }
}
