package http.request;

public class HttpRequestBody {
    private final String content;

    HttpRequestBody(String content) {
        this.content = content;
    }

    public String content() { return content; }
}
