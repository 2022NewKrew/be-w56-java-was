package http.request;

public class HttpRequestBody {
    private final String content;

    public HttpRequestBody(String content) {
        this.content = content;
    }

    public static HttpRequestBody empty() {
        return new HttpRequestBody("");
    }

    @Override
    public String toString() {
        return "HttpRequestBody{" +
                "content='" + content + '\'' +
                '}';
    }
}
