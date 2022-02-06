package http.request;

public class RawRequestBody {
    private final String value;

    RawRequestBody(String value) {
        this.value = value;
    }

    public static RawRequestBody empty() {
        return new RawRequestBody("");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "value='" + value + '\'' +
                '}';
    }
}
