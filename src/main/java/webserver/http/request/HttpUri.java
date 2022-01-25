package webserver.http.request;

public class HttpUri {
    private final String value;

    public HttpUri(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HttpUri{" +
                "value='" + value + '\'' +
                '}';
    }
}
