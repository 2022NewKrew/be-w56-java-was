package webserver.request;

public class RequestBody {
    private final String body;

    private RequestBody(String body) {
        this.body = body;
    }

    public static RequestBody from(String body) {
        return new RequestBody(body);
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "body='" + body + '\'' +
                '}';
    }
}
