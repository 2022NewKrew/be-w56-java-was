package http;

public class Path {
    private final String value;

    public Path(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String createHeader() {
        return "Path=" + value;
    }
}
