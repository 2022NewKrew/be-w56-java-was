package infrastructure.model;

import java.nio.charset.StandardCharsets;

public class HttpStringBody implements HttpBody {

    private final String value;

    public HttpStringBody(String value) {
        this.value = value;
    }

    @Override
    public byte[] toByteStream() {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }
}
